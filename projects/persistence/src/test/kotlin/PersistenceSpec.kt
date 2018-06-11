import grounded.DatabaseConfig
import jsoning.loadJsonFile
import junit.framework.TestCase.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import pando.createNewBlockchain
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

    it("can save and load a blockchain") {
      val pair = generateAddressPair()
      val newBlockchain = createNewBlockchain(pair.address, pair.keyPair.public)
      db.saveBlockchain(newBlockchain)
      val data = db.loadBlockchain(newBlockchain.address)

      assertNotNull("DB response should not be null", data)
      assertEquals("DB response should include the correct address", newBlockchain.address, data!!.address)
    }

    it("returns null when trying to load a nonexistent blockchain") {
      val pair = generateAddressPair()
      val data = db.loadBlockchain(pair.address)
      assertEquals("Should return null when there is no matching DB entry", null, data)
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

  }
})