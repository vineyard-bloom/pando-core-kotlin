import grounded.DatabaseConfig
import jsoning.loadJsonFile
import junit.framework.TestCase.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import pando.*
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

      // does it need to have a tx to run loadBlock??
      val pair = generateAddressPair()
      val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)

      // try using mintTokens

//      val transaction1 = createTransaction(1000, pair.address, null)
//      val block1 = createBlock(blockchain, transaction1, pair.keyPair.private)
//      println("block1 index is ${block1.index}")
//      // Must update blockchain so we know there's a block there now
//      val transaction2 = createTransaction(2000, pair.address, null)
//      val block2 = createBlock(blockchain, transaction2, pair.keyPair.private)
//      println("block2 index is ${block2.index}")

      db.saveBlockchain(blockchain)
      db.saveBlock(block1)
      db.saveTransaction(transaction1)
      db.saveBlock(block2)
      db.saveTransaction(transaction2)

      val blockchainData = db.loadBlockchain(blockchain.address)
      println("blockchain data: $blockchainData")

      assertNotNull("DB response should not be null", blockchainData)
      assertEquals("DB response should include the correct address", blockchain.address, blockchainData!!.address)
    }

    it("can save and load a block") {
      val pair = generateAddressPair()
      val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
      val transaction = createTransaction(1000, pair.address, null)
      val block = createBlock(blockchain, transaction, pair.keyPair.private)

      db.saveBlockchain(blockchain)
      db.saveBlock(block)
      db.saveTransaction(transaction)
      val blockData = db.loadBlock(block.index)

      assertNotNull("DB response should not be null", blockData)
      assertEquals("DB response should include the correct hash", block.hash, blockData!!.hash)
      assertEquals("DB response should include the correct address", block.address, blockData!!.address)
    }

    it("can save and load a transaction") {
      val pair = generateAddressPair()
      val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
      val transaction = createTransaction(1000, pair.address, null)
      val block = createBlock(blockchain, transaction, pair.keyPair.private)

      db.saveBlockchain(blockchain)
      db.saveBlock(block)
      db.saveTransaction(transaction)

      val transactionData = db.loadTransaction(transaction.hash)

      assertNotNull("DB response should not be null", transactionData)
      assertEquals("DB response should include the correct hash", transaction.hash, transactionData!!.hash)
      assertEquals("DB response should include the correct address", transaction.to, transactionData!!.to)
    }

  }
})