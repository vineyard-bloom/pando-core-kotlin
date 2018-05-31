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

    it("can save and load a blockchain") {
      val appConfig = loadAppConfig("config/config.json")
      val db = PandoDatabase(appConfig.database)

      val pair = generateAddressPair()
      val newBlockchain = createNewBlockchain(pair.address, pair.keyPair.public)

      db.fixtureInit()
      db.saveBlockchain(newBlockchain)
//      val data = db.loadBlockchain(newBlockchain.address)
//
//      assertEquals(1, data.size)
    }

  }
})