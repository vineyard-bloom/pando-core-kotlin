package persistence

import grounded.DatabaseConfig
import grounded.createDataSource
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.sql.Connection
import pando.*

object Blockchains : Table() {
  val address = varchar("address", 40).primaryKey()
  val publicKey = varchar("publicKey", 375)
  val created = datetime("created")
  val modified = datetime("modified")
}

object Blocks : Table() {
  val hash = varchar("hash", 64).primaryKey()
  val index = long("index")
  val address = (varchar("address", 40) references Blockchains.address)
  val transactionHash = varchar("transactionHash", 64).uniqueIndex()
  val previousBlock = varchar("previousBlock", 64).nullable()
  val createdAt = datetime("createdAt")
  val created = datetime("created")
  val modified = datetime("modified")
}

object Transactions : Table() {
  val hash = (varchar("hash", 64).primaryKey() references Blocks.transactionHash)
  val value = long("value")
  val to = varchar("to", 40)
  val from = varchar("from", 40).nullable()
  val created = datetime("created")
  val modified = datetime("modified")
}

object Signatures: Table() {
  val signer = varchar("signer", 40)
  val publicKey = varchar("publicKey", 375)
  val signature = text("signature")
  val blockHash = (varchar("blockHash", 64).primaryKey() references Blocks.hash)
  val created = datetime("created")
  val modified = datetime("modified")
}

data class AppConfig(
  val database: DatabaseConfig
)


class PandoDatabase(private val config: DatabaseConfig) {
  private val source = createDataSource(config)

  fun fixtureInit() {
    Database.connect(source)
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

    transaction {
      logger.addLogger(StdOutSqlLogger)

      drop(Signatures)
      drop(Transactions)
      drop(Blocks)
      drop(Blockchains)

      create(Blockchains)
      create(Blocks)
      create(Transactions)
      create(Signatures)
    }
  }

  fun saveBlockchain(blockchain: Blockchain) {
    Database.connect(source)
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

    transaction {
      logger.addLogger(StdOutSqlLogger)

      Blockchains.insert {
        it[address] = blockchain.address
        it[publicKey] = keyToString(blockchain.publicKey)
        it[created] = DateTime.now()
        it[modified] = DateTime.now()
      }
    }

    blockchain.blocks.map { saveBlock(it!!) }
    blockchain.blocks.map { saveTransaction(it!!.transaction) }
    blockchain.blocks.map {
      block -> block!!.blockSignatures.map { signature -> saveSignature(signature, block) }
    }
  }

  fun loadBlockchain(address: Address): Blockchain? {
    Database.connect(source)
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

    val blockchain = transaction {
      logger.addLogger(StdOutSqlLogger)

      Blockchains.select { Blockchains.address eq address }.map {
        Blockchain(
            it[Blockchains.address],
            stringToPublicKey(it[Blockchains.publicKey]),
            loadBlocks(address)
        )
      }
    }

    if (blockchain.isEmpty()) {
      return null
    }
    return blockchain.first()
  }

  fun loadBlockchains(): List<Blockchain?> {
    Database.connect(source)
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

    return transaction {
      logger.addLogger(StdOutSqlLogger)

      Blockchains.selectAll().map {
        Blockchain(
            it[Blockchains.address],
            stringToPublicKey(it[Blockchains.publicKey]),
            loadBlocks(it[Blockchains.address])
        )
      }
    }
  }

  fun saveBlock(block: Block) {
    Database.connect(source)
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

    transaction {
      logger.addLogger(StdOutSqlLogger)

      Blocks.insert {
        it[hash] = block.hash
        it[index] = block.index
        it[address] = block.address
        it[transactionHash] = block.transaction.hash
        it[previousBlock] = if (block.previousBlock != null) block.previousBlock!!.hash else null
        it[createdAt] = block.createdAt
        it[created] = DateTime.now()
        it[modified] = DateTime.now()
      }
    }
  }

  fun loadBlock(hash: String): Block? {
    Database.connect(source)
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

    val block = transaction {
      logger.addLogger(StdOutSqlLogger)

      Blocks.select { Blocks.hash eq hash }.map {
        Block(
            it[Blocks.hash],
            BlockContents(
                it[Blocks.index],
                it[Blocks.address],
                loadTransaction(it[Blocks.transactionHash])!!,
                loadBlock(Blocks.previousBlock.toString()),
                it[Blocks.createdAt]
            ),
            loadSignatures(it[Blocks.hash])
        )
      }
    }

    if (block.isEmpty()) {
      return null
    }
    return block.first()
  }

  fun loadBlocks(address: Address): List<Block?> {
    Database.connect(source)
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

    return Blocks.select { Blocks.address eq address }.map {
      loadBlock(it[Blocks.hash])
    }
  }

  fun saveTransaction(transaction: BaseTransaction) {
    Database.connect(source)
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

    transaction {
      logger.addLogger(StdOutSqlLogger)

      Transactions.insert {
        it[hash] = transaction.hash
        it[value] = transaction.value as Long
        it[to] = transaction.to
        it[from] = transaction.from
        it[created] = DateTime.now()
        it[modified] = DateTime.now()
      }
    }
  }

  fun loadTransaction(hash: String): BaseTransaction? {
    Database.connect(source)
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

    val transaction = transaction {
      logger.addLogger(StdOutSqlLogger)

      Transactions.select { Transactions.hash eq hash }.map {
        BaseTransaction(
            it[Transactions.hash],
            TransactionContent(
                it[Transactions.value],
                it[Transactions.to],
                it[Transactions.from]
            )
        )
      }
    }

    if (transaction.isEmpty()) {
      return null
    }
    return transaction.first()
  }

  fun saveSignature(blockSignature: BlockSignature, block: Block) {
    Database.connect(source)
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

    transaction {
      logger.addLogger(StdOutSqlLogger)

      Signatures.insert {
        it[signer] = blockSignature.signer
        it[publicKey] = keyToString(blockSignature.publicKey)
        // byte array conversion causing issues
        it[signature] = blockSignature.signature.toString()
        it[blockHash] = block.hash
        it[created] = DateTime.now()
        it[modified] = DateTime.now()
      }
    }
  }

  fun loadSignatures(blockHash: String): List<BlockSignature> {
    Database.connect(source)
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

    return transaction {
      logger.addLogger(StdOutSqlLogger)

      Signatures.select { Signatures.blockHash eq blockHash }.map {
        BlockSignature(
            it[Signatures.signer],
            stringToPublicKey(it[Signatures.publicKey]),
            // byte array conversion causing issues
            it[Signatures.signature].toByteArray()
        )
      }
    }
  }

}
