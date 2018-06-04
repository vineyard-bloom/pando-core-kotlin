package persistence

import grounded.DatabaseConfig
import grounded.createDataSource
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import pando.Address
import pando.Block
import pando.Blockchain

data class BlockchainData(
  val id: Int,
  val address: String,
  val publicKey: String,
  val created: DateTime,
  val modified: DateTime
)

data class BlockData(
  val hash: String,
  val index: Long,
  val address: String,
  val previousBlock: Long?,
  val created: DateTime,
  val modified: DateTime
)

object Blockchains : Table() {
  val id = integer("id").autoIncrement().uniqueIndex()
  val address = varchar("address", 40).primaryKey()
  val publicKey = varchar("publicKey", 375)
  val created = datetime("created")
  val modified = datetime("modified")
}

object Blocks : Table() {
  val hash = varchar("hash", 64)
  val index = long("index").primaryKey()
  val address = (varchar("address", 40) references Blockchains.address)
  val previousBlock = long("previousBlock").nullable()
  val created = datetime("created")
  val modified = datetime("modified")
}

class PandoDatabase(private val config: DatabaseConfig) {
  private val source = createDataSource(config)

  fun fixtureInit() {
    Database.connect(source)

    transaction {
      drop(Blocks)
      drop(Blockchains)

      create(Blockchains)
      create(Blocks)
    }
  }

  fun saveBlockchain(blockchain: Blockchain) {
    Database.connect(source)

    transaction {
      Blockchains.insert {
        it[address] = blockchain.address
        it[publicKey] = blockchain.publicKey.toString()
        it[created] = DateTime.now()
        it[modified] = DateTime.now()
      }
    }
  }

  fun loadBlockchain(address: Address): BlockchainData? {
    val blockchain = transaction {
      Blockchains.select { Blockchains.address eq address }.map {
        BlockchainData(it[Blockchains.id], it[Blockchains.address], it[Blockchains.publicKey], it[Blockchains.created], it[Blockchains.modified])
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
      Blocks.insert {
        it[hash] = block.hash
        it[index] = block.index
        it[address] = block.address
        it[previousBlock] = if (block.previousBlock != null) block.previousBlock!!.index else null
        it[created] = DateTime.now()
        it[modified] = DateTime.now()
      }
    }
  }

  fun loadBlock(index: Long): BlockData? {
    val block = transaction {
      Blocks.select { Blocks.index eq index }.map {
        BlockData(it[Blocks.hash], it[Blocks.index], it[Blocks.address], it[Blocks.previousBlock], it[Blocks.created], it[Blocks.modified])
      }
    }

    if (block.isEmpty()) {
      return null
    }
    return block.first()
  }
}

