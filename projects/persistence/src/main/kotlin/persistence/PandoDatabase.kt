package persistence

import grounded.DatabaseConfig
import grounded.createDataSource
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop
import org.jetbrains.exposed.sql.transactions.transaction
import pando.Address
import pando.Blockchain

object Blockchains : Table() {
  val id = integer("id").autoIncrement().uniqueIndex()
  val address = varchar("address", 40).primaryKey()
  val publicKey = varchar("publicKey", 375)
}

data class BlockchainData(
  val id: Int,
  val address: String,
  val publicKey: String
)

class PandoDatabase(private val config: DatabaseConfig) {
  private val source = createDataSource(config)

  fun fixtureInit() {
    Database.connect(source)

    transaction {
      drop(Blockchains)
      create(Blockchains)
    }
  }

  fun saveBlockchain(blockchain: Blockchain) {
    Database.connect(source)

    transaction {
      Blockchains.insert {
        it[address] = blockchain.address
        it[publicKey] = blockchain.publicKey.toString()
      }
    }
  }

  fun loadBlockchain(address: Address): BlockchainData? {
    val blockchain = transaction {
      Blockchains.select { Blockchains.address eq address }.limit(1).map {
        BlockchainData(it[Blockchains.id], it[Blockchains.address], it[Blockchains.publicKey])
      }
    }
    return blockchain.first()
  }
}

