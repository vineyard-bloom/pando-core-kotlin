package persistence

import grounded.DatabaseConfig
import grounded.createDataSource
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import pando.*

data class BlockchainData(
  val address: String,
  val blocks: List<BlockData>
)

data class BlockData(
  val hash: String,
  val index: Long,
  val address: String,
  val createdAt: DateTime
)

object Blockchains : Table() {
  val id = integer("id").autoIncrement().uniqueIndex()
  val address = varchar("address", 40).primaryKey()
  val created = datetime("created")
  val modified = datetime("modified")
}

object Blocks : Table() {
  val hash = varchar("hash", 64)
  val index = long("index").primaryKey()
  val address = (varchar("address", 40) references Blockchains.address)
  val previousBlock = long("previousBlock").nullable()
  val createdAt = datetime("createdAt")
  val created = datetime("created")
  val modified = datetime("modified")
}

object Transactions : Table() {
  val id = integer("id").autoIncrement().uniqueIndex()
  val hash = varchar("hash", 64)
  val value = long("value")
  val to = varchar("to", 40)
  val from = varchar("from", 40).nullable()
  val created = datetime("created")
  val modified = datetime("modified")
}

class PandoDatabase(private val config: DatabaseConfig) {
  private val source = createDataSource(config)

  fun fixtureInit() {
    Database.connect(source)

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

    transaction {
      logger.addLogger(StdOutSqlLogger)

      Blockchains.insert {
        it[address] = blockchain.address
        it[created] = DateTime.now()
        it[modified] = DateTime.now()
      }
    }
  }

  fun loadBlockchain(address: Address): BlockchainData? {
    val blockList = transaction {
      logger.addLogger(StdOutSqlLogger)

      Blocks.select { Blocks.address eq address }.map {
        BlockData(
            it[Blocks.hash],
            it[Blocks.index],
            it[Blocks.address],
            DateTime.parse(it[Blocks.createdAt].toString())
        )
      }
    }

    val blockchain = transaction {
      logger.addLogger(StdOutSqlLogger)

      Blockchains.select { Blockchains.address eq address }.map {
        BlockchainData(
            it[Blockchains.address],
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

    transaction {
      logger.addLogger(StdOutSqlLogger)

      Blocks.insert {
        it[hash] = block.hash
        it[index] = block.index
        it[address] = block.address
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

      Blocks.select { Blocks.index eq index }.map {
        BlockData(
            it[Blocks.hash],
            it[Blocks.index],
            it[Blocks.address],
            DateTime.parse(it[Blocks.createdAt].toString())
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
        it[value] = transaction.value
        it[to] = transaction.to
        it[to] = transaction.from
        it[created] = DateTime.now()
        it[modified] = DateTime.now()
      }
    }
  }

}

