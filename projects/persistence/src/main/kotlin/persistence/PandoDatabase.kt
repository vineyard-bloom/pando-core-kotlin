package persistence

import grounded.DatabaseConfig
import grounded.createDataSource
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import pando.Address
import pando.Block
import pando.Blockchain
import java.sql.Connection
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.text.SimpleDateFormat
import pando.*

data class BlockchainData(
  val address: String,
  val publicKey: String,
  val blocks: List<BlockData?>
)

data class BlockData(
  val hash: String,
  val index: Long,
  val address: String,
  val transaction: TransactionData,
  val createdAt: DateTime
)

data class TransactionData(
  val hash: String,
  val value: String,
  val to: String,
  val from: String?
)

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
  val previousBlock = long("previousBlock").nullable()
  val createdAt = datetime("createdAt")
  val created = datetime("created")
  val modified = datetime("modified")
}

object Transactions : Table() {
  val id = integer("id").autoIncrement().uniqueIndex()
  val hash = (varchar("hash", 64).primaryKey() references Blocks.transactionHash)
  val value = text("value")
  val to = varchar("to", 40)
  val from = varchar("from", 40).nullable()
  val created = datetime("created")
  val modified = datetime("modified")
}

class PandoDatabase(private val config: DatabaseConfig) {
  private val source = createDataSource(config)

  fun fixtureInit() {
    Database.connect(source)
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

    transaction {
      logger.addLogger(StdOutSqlLogger)

      drop(Transactions)
      drop(Blocks)
      drop(Blockchains)

      create(Blockchains)
      create(Blocks)
      create(Transactions)
    }
  }

  fun saveBlockchain(blockchain: Blockchain) {
    Database.connect(source)
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

    transaction {
      logger.addLogger(StdOutSqlLogger)

      Blockchains.insert {
        it[address] = blockchain.address
        it[publicKey] = blockchain.publicKey.toString()
        it[created] = DateTime.now()
        it[modified] = DateTime.now()
      }
    }
  }

  fun loadBlockchain(address: Address): BlockchainData? {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    val blocks = transaction {
      logger.addLogger(StdOutSqlLogger)

      Blocks.select { Blocks.address eq address }.toList()
    }

    val blockchain = transaction {
      Blockchains.select { Blockchains.address eq address }.map {
        BlockchainData(
            it[Blockchains.address],
            it[Blockchains.publicKey],
            blockList
        )
      }
    }

    if (blockchain.isEmpty()) {
      return null
    }
    return blockchain.first()
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
        it[previousBlock] = if (block.previousBlock != null) block.previousBlock!!.index else null
        it[createdAt] = DateTime.parse(block.createdAt.toString())
        it[created] = DateTime.now()
        it[modified] = DateTime.now()
      }
    }
  }

  fun loadBlock(index: Long): BlockData? {
    val block = transaction {
      logger.addLogger(StdOutSqlLogger)

      (Blocks innerJoin Transactions).slice(
          Blocks.hash,
          Blocks.index,
          Blocks.address,
          Blocks.createdAt,
          Transactions.hash,
          Transactions.value,
          Transactions.to,
          Transactions.from
      ).select { Blocks.index.eq(index) and Blocks.transactionHash.eq(Transactions.hash) }.map {
        BlockData(
            it[Blocks.hash],
            it[Blocks.index],
            it[Blocks.address],
            LocalDateTime.parse(it[Blocks.createdAt].toString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            TransactionData(
                it[Transactions.hash],
                it[Transactions.value],
                it[Transactions.to],
                it[Transactions.from]
            ),
            it[Blocks.createdAt]
        )
      }
    }

    if (block.isEmpty()) {
      return null
    }
    return block.first()
  }

  fun saveTransaction(transaction: BaseTransaction) {
    Database.connect(source)

    transaction {
      logger.addLogger(StdOutSqlLogger)

      Transactions.insert {
        it[hash] = transaction.hash
        it[value] = transaction.value.toString()
        it[to] = transaction.to
        it[from] = transaction.from
        it[created] = DateTime.now()
        it[modified] = DateTime.now()
      }
    }
  }

  fun loadTransaction(hash: String): TransactionData? {
    val transaction = transaction {
      logger.addLogger(StdOutSqlLogger)

      Transactions.select { Transactions.hash eq hash }.map {
        TransactionData(
            it[Transactions.hash],
            it[Transactions.value],
            it[Transactions.to],
            it[Transactions.from]
        )
      }
    }

    if (transaction.isEmpty()) {
      return null
    }
    return transaction.first()
  }

}

