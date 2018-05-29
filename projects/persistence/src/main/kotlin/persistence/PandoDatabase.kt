package persistence

import grounded.DatabaseConfig
import grounded.createDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import pando.Address
import pando.Blockchain

object Blockchains : Table() {
  val address = varchar("id", 255).primaryKey()
}

class PandoDatabase(private val config: DatabaseConfig) {
  private val source = createDataSource(config)

  fun populateSchema() {
    Database.connect(source)

    transaction {
      create(Blockchains)
    }
  }

  fun saveBlockchain(blockchain: Blockchain) {
    Database.connect(source)

    transaction {
      Blockchains.insert {
        it[address] = blockchain.address
      }
    }
  }

  fun loadBlockchain(address: Address): Blockchain? {
    throw Error("Not implemented.")
  }
}

