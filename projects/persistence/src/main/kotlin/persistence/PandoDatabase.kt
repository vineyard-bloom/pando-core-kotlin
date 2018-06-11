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

data class BlockchainData(
  val address: String,
  val publicKey: String,
  val blocks: List<BlockData>
)

data class BlockData(
  val hash: String,
  val index: Long,
  val address: String,
  val createdAt: LocalDateTime
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
  val previousBlock = long("previousBlock").nullable()
  val createdAt = datetime("createdAt")
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

      drop(Blocks)
      drop(Blockchains)

      create(Blockchains)
      create(Blocks)
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
    val blockList = transaction {
      logger.addLogger(StdOutSqlLogger)

      Blocks.select { Blocks.address eq address }.map {
        BlockData(
            it[Blocks.hash],
            it[Blocks.index],
            it[Blocks.address],
            LocalDateTime.parse(it[Blocks.createdAt].toString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        )
      }
    }

    val blockchain = transaction {
//      logger.addLogger(StdOutSqlLogger)

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
            LocalDateTime.parse(it[Blocks.createdAt].toString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        )
      }
    }

    if (block.isEmpty()) {
      return null
    }
    return block.first()
  }
}

