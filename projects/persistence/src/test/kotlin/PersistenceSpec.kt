import grounded.DatabaseConfig
import jsoning.loadJsonFile
import junit.framework.TestCase.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import pando.createNewBlockchain
import pando.createTransaction
import pando.generateAddressPair
import pando.mintTokens
import persistence.PandoDatabase

data class AppConfig(
  val database: DatabaseConfig
)

fun loadAppConfig(path: String): AppConfig =
  loadJsonFile<AppConfig>(path)

class PersistenceSpec : Spek({
  describe("persistence") {
    val appConfig = loadAppConfig("config/config.json")
    val db = PandoDatabase(appConfig.database)
    db.fixtureInit()

    it("returns null when trying to load nonexistent data") {
      val blockchainData = db.loadBlockchain("Fake address")
      assertNull("Should return null when there is no matching blockchain", blockchainData)

      val blockData = db.loadBlock(5392)
      assertNull("Should return null when there is no matching block", blockData)

      val transactionData = db.loadTransaction("07F1896758704287DA11FCB8BB70350020A357ADCB9C441D3CE9C900F2E8C6E5")
      assertNull("Should return null when there is no matching blockchain", transactionData)
    }


    it("can save and load a blockchain") {
      val pair = generateAddressPair()
      val newBlockchain = createNewBlockchain(pair.address, pair.keyPair.public)
      db.saveBlockchain(newBlockchain)
      val data = db.loadBlockchain(newBlockchain.address)

      assertNotNull("DB response should not be null", data)
      assertEquals("DB response should include the correct address", newBlockchain.address, data!!.address)
    }

    it("can save and load a block") {
      val pair = generateAddressPair()
      val newBlockchain =  createNewBlockchain(pair.address, pair.keyPair.public)
      val updatedBlockchain = mintTokens(newBlockchain, 1000)
      val block = updatedBlockchain.blocks.first()

      db.saveBlockchain(updatedBlockchain)
      db.saveBlock(block)
      val data = db.loadBlock(block.index)

      assertNotNull("DB response should not be null", data)
      assertEquals("DB response should include the correct hash", block.hash, data!!.hash)
      assertEquals("DB response should include the correct address", block.address, data!!.address)
    }

    it("can save and load a transaction") {
      val pair = generateAddressPair()
      val transaction = createTransaction(1000, pair.address, null)

      db.saveTransaction(transaction)
      val data = db.loadTransaction(transaction.hash)

      assertNotNull("DB response should not be null", data)
      assertEquals("DB response should include the correct hash", transaction.hash, data!!.hash)
      assertEquals("DB response should include the correct address", transaction.to, data!!.to)
    }

  }
})