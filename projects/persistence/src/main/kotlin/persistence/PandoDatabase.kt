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
  val id: Column<Int> = integer("id").autoIncrement().uniqueIndex()
  val address: Column<String> = varchar("id", 255).primaryKey()
  val balance: Column<Int> = integer("balance")
  // val date: Column<DateTime> = datetime("created")
  // val modified: Column<DateTime> = datetime("modified")
}

class PandoDatabase(private val config: DatabaseConfig) {
  private val source = createDataSource(config)

  fun fixtureInit() {
    Database.connect(source)

    transaction {
      create(Blockchains)
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

