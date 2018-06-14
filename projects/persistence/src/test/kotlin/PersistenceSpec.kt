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

    // Generate some data
    val pair = generateAddressPair()
    val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
    val updatedBlockchain = mintTokens(blockchain, 1000)
    val newestBlockchain = mintTokens(updatedBlockchain, 2000)
    val signature = sign(pair.keyPair.private, "Some random data")
    val blockSignature = BlockSignature(pair.address, pair.keyPair.public, signature)

    // Save the data
    db.saveBlockchain(newestBlockchain)
    newestBlockchain.blocks.map { block -> db.saveBlock(block!!) }
    newestBlockchain.blocks.map { block -> db.saveTransaction(block!!.transaction) }
    db.saveSignature(blockSignature, updatedBlockchain.blocks.first()!!)

    it("returns null when trying to load nonexistent data") {
      val blockchainData = db.loadBlockchain("Fake address")
      assertNull("Should return null when there is no matching blockchain", blockchainData)

      val blockData = db.loadBlock("Fake hash")
      assertNull("Should return null when there is no matching block", blockData)

      val transactionData = db.loadTransaction("07F1896758704287DA11FCB8BB70350020A357ADCB9C441D3CE9C900F2E8C6E5")
      assertNull("Should return null when there is no matching blockchain", transactionData)
    }

    it("can load a blockchain") {
      val blockchainData = db.loadBlockchain(newestBlockchain.address)
      assertNotNull("DB response should not be null", blockchainData)
      assertEquals("DB response should include the correct address", newestBlockchain.address, blockchainData!!.address)
      assertEquals("Blockchain should be associated with two blocks", 2, blockchainData!!.blocks.size)
      assertEquals("DB response should include the correct publicKey", newestBlockchain.publicKey, blockchainData!!.publicKey)
    }

    it("can load a block") {
      val blockData = db.loadBlock(newestBlockchain.blocks.first()!!.hash)
      assertNotNull("DB response should not be null", blockData)
      assertNotNull("DB response should include a signatures list that is not empty", blockData!!.blockSignatures.first())
      assertEquals("DB response should include the correct hash", newestBlockchain.blocks.first()!!.hash, blockData!!.hash)
      assertEquals("DB response should include the correct address", newestBlockchain.blocks.first()!!.address, blockData!!.address)
//      assertEquals("DB response should include the correct signature data", blockSignature, blockData!!.blockSignatures.first()
    }

    it("can load a transaction") {
      val transactionData = db.loadTransaction(updatedBlockchain.blocks.first()!!.transaction.hash)
      assertNotNull("DB response should not be null", transactionData)
      assertEquals("DB response should include the correct hash", updatedBlockchain.blocks.first()!!.transaction.hash, transactionData!!.hash)
      assertEquals("DB response should include the correct 'to'", updatedBlockchain.blocks.first()!!.transaction.to, transactionData!!.to)
    }

    it("can save and load a signature") {
      val signatureData = db.loadSignatures(updatedBlockchain.blocks.first()!!.hash)

      assertNotNull("DB response should not be null", signatureData)
      assertEquals("DB response should include the correct signer", blockSignature.signer, signatureData.first()!!.signer)
//      assertEquals("DB response should include the correct signature", signature, signatureData.first()!!.signature)
      assertEquals("DB response should include the correct publicKey", blockSignature.publicKey, signatureData.first()!!.publicKey)
    }

  }
})