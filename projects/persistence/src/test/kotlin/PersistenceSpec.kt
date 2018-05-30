import grounded.DatabaseConfig
import jsoning.loadJsonFile
import junit.framework.TestCase.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import persistence.DatabaseConfig
import persistence.PandoDatabase

data class AppConfig(
    val database: DatabaseConfig
)

fun loadAppConfig(path: String): AppConfig =
    loadJsonFile<AppConfig>(path)

class PersistenceSpec : Spek({
  describe("persistence") {

    it("can save and load a blockchain") {
      val config = loadAppConfig("config/config.json")
      val db = PandoDatabase(config)

      db.fixtureInit()
      db.saveBlockchain()
      db.loadBlockchain()

    }

  }
})