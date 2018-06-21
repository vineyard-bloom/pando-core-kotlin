import junit.framework.TestCase.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import pando.*
import serving.createServer
import java.util.concurrent.TimeUnit
import clienting.getBlockchain
import clienting.postBlockchain
import jsoning.loadJsonFile
import persistence.AppConfig
import persistence.PandoDatabase
import serving.Server


class ServerSpec : Spek({

  fun loadAppConfig(path: String): AppConfig =
    loadJsonFile<AppConfig>(path)

  fun initDatabase(): Pair<BlockchainSource, BlockchainConsumer> {
    val appConfig = loadAppConfig("config/config.json")
    val db = PandoDatabase(appConfig.database)
    db.fixtureInit()
    val source = Pair({ address:Address -> db.loadBlockchain(address) }, { blockchain: Blockchain -> Unit })
    return source
  }

  fun fullTest(source: BlockchainSource, consumer: BlockchainConsumer): Server {
    val server = createServer(source, consumer)
    return server
  }

  describe("server requests") {

    it("can get blockchain from address") {

      val pair = generateAddressPair()
      val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
      val source = { address: Address -> blockchain }
      val consumer = { blockchain: Blockchain -> Unit }
      val server = fullTest(source, consumer)
      val res = getBlockchain(blockchain.address)
      server.stop(1000, 30, TimeUnit.SECONDS) // Not needed but a nicety

      assertEquals(blockchain.address, res.address)
    }

    it("can post blockchain") {

      val pair = generateAddressPair()
      val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
      val source = { address: Address -> null }
      val consumer = { blockchain: Blockchain -> Unit }
      fullTest(source, consumer)
      val res = postBlockchain(blockchain)
      assertEquals(blockchain.address, res.address)
    }

  }

  describe("server requests with persistance") {

    it("can get blockchain from address") {

      val pair = generateAddressPair()
      val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
      val source = initDatabase()
      val server = fullTest(source.first, source.second)
      val res = getBlockchain(blockchain.address)
      server.stop(1000, 30, TimeUnit.SECONDS) // Not needed but a nicety

      assertEquals(blockchain.address, res.address)
    }

    it("can post blockchain") {

      val pair = generateAddressPair()
      val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
      val source = initDatabase()
      fullTest(source.first, source.second)
      val res = postBlockchain(blockchain)
      assertEquals(blockchain.address, res.address)
    }

  }
})