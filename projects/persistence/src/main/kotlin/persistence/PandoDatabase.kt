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
import pando.Hash

data class BlockchainData(
  val id: Int,
  val address: String,
  val publicKey: String,
  val created: DateTime,
  val modified: DateTime
)

data class BlockData(
  val hash: String,
  val index: Int,
  val address: String,
  val previousBlock: Int,
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
  val index = integer("index").autoIncrement().uniqueIndex().primaryKey()
  val address = (varchar("address", 40) references Blockchains.address)
  val previousBlock = integer("previousBlock")
  val created = datetime("created")
  val modified = datetime("modified")
}

class PandoDatabase(private val config: DatabaseConfig) {
  private val source = createDataSource(config)

  fun fixtureInit() {
    Database.connect(source)

    transaction {
      drop(Blockchains)
      drop(Blocks)

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
//        it[address] = blockchain.address
//        it[publicKey] = blockchain.publicKey.toString()
//        it[created] = DateTime.now()
//        it[modified] = DateTime.now()
        it[hash] = block.hash
        it[index] = block.index
        it[address] = block.address
        it[previousBlock] = block.index - 1
        it[created] = DateTime.now()
        it[modified] = DateTime.now()
      }
    }
  }

  fun loadBlocks(hash: String): BlockData? {
    throw Error("not implemented")
  }
}

