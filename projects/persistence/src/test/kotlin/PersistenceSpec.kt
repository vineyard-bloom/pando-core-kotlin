import grounded.DatabaseConfig
import jsoning.loadJsonFile
import junit.framework.TestCase.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import pando.createNewBlockchain
import pando.generateAddressPair
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

      println("Returned data is: $data")

      assert(data != null)
      assertEquals(data!!.address, newBlockchain.address)
      assertEquals(data!!.publicKey, newBlockchain.publicKey.toString())
    }

    it("returns null when trying to load a nonexistent address") {
      val pair = generateAddressPair()

      db.loadBlockchain(pair.address)

      // TODO should return null

    }

    it("throws an error when trying to save a duplicate address") {
      val pair = generateAddressPair()
      val newBlockchain = createNewBlockchain(pair.address, pair.keyPair.public)

      db.saveBlockchain(newBlockchain)
      db.saveBlockchain(newBlockchain)

      // TODO should throw error
    }

  }
})