import junit.framework.TestCase.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import pando.*
import serving.createServer
import java.util.concurrent.TimeUnit
import clienting.getBlockchain
import clienting.postBlockchain
import io.ktor.http.HttpStatusCode
import jsoning.loadJsonFile
import persistence.AppConfig
import persistence.PandoDatabase
import serving.Server
import java.security.PrivateKey


class ServerSpec : Spek({

  fun transaction(blockchainOne: Blockchain, blockchainTwo: Blockchain, privateKey: PrivateKey):Pair<Blockchain, Blockchain> {
    val send = sendTokens(blockchainOne, blockchainTwo, 0, privateKey)
    val (blockOne, _) = validateBlock(send.first(), blockchainOne.publicKey, blockchainOne)
    val (blockTwo, _) = validateBlock(send.last(), blockchainOne.publicKey, blockchainOne)
    return Pair(addBlockWithoutValidation(blockchainOne, blockOne!!), addBlockWithoutValidation(blockchainTwo, blockTwo!!))
  }

  fun loadAppConfig(path: String): AppConfig =
    loadJsonFile<AppConfig>(path)

  fun initSources(): Pair<BlockchainSource, BlockchainConsumer> {
    val appConfig = loadAppConfig("config/config.json")
    val db = PandoDatabase(appConfig.database)
    db.fixtureInit()
    val source = Pair({ address:Address -> db.loadBlockchain(address) }, { blockchain: Blockchain -> db.saveBlockchain(blockchain) })
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
      val url = "http://0.0.0.0:8080"
      val res = getBlockchain(url, blockchain.address)
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
//      assertEquals(blockchain.address, res.address)
    }

  }

  describe("server requests with persistence") {

    it("can get blockchain from address") {
      val pair = generateAddressPair()
      val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
      val source = initSources()
      source.second(blockchain)
      val server = fullTest(source.first, source.second)
      val url = "http://0.0.0.0:8080"
      val res = getBlockchain(url, blockchain.address)
      server.stop(1000, 30, TimeUnit.SECONDS) // Not needed but a nicety

      assertEquals(blockchain.address, res.address)
    }

    it("can post blockchain with persistence") {

      val pair = generateAddressPair()
      val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
      val source = initSources()
      val server = fullTest(source.first, source.second)
      val res = postBlockchain(blockchain)
      assertEquals(HttpStatusCode.OK, res)
    }

    it("can add blocks when sent a blockchain") {
      val source = initSources()
      val server = fullTest(source.first, source.second)
      val url = "http://0.0.0.0:8080"
      val (blockchainOne, privateKey) = utility.createNewBlockchain()
      val (blockchainTwo, _) = utility.createNewBlockchain()
      postBlockchain(blockchainTwo)
      val transactionOne = transaction(blockchainOne, blockchainTwo, privateKey)
      postBlockchain(transactionOne.first)
      postBlockchain(transactionOne.second)
      val newBlockchainA = transactionOne.first
      val newBlockchainB = transactionOne.second
      val transactionTwo = transaction(newBlockchainA, newBlockchainB, privateKey)
      postBlockchain(transactionTwo.first)
      postBlockchain(transactionTwo.second)

      val transactionTwoA = getBlockchain(url, transactionTwo.first.address)
      val transactionTwoB = getBlockchain(url, transactionTwo.second.address)
      assertEquals(2, transactionTwoA!!.blocks.size)
      assertEquals(2, transactionTwoB!!.blocks.size)
    }

  }
})