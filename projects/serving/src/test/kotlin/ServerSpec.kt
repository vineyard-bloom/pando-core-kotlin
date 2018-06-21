import junit.framework.TestCase.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import pando.*
import serving.createServer
import java.net.URL
import java.util.concurrent.TimeUnit
import jsoning.parseJson
import networking.BlockchainData
import clienting.getBlockchain
import clienting.postBlockchain
import serving.Server


class ServerSpec : Spek({

  fun fullTest(blockchains: BlockchainSource): Server {
    val server = createServer(blockchains)
    return server
  }
  describe("server requests") {

    it("can get blockchain from address") {

      val pair = generateAddressPair()
      val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
      val source = { address: Address -> blockchain }
      val server = fullTest(source)
      val res = getBlockchain(blockchain.address)
      server.stop(1000, 30, TimeUnit.SECONDS) // Not needed but a nicety

      assertEquals(blockchain.address, res.address)
    }

    it("can post blockchain") {

      val pair = generateAddressPair()
      val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
      val source = { address: Address -> null }
      fullTest(source)
      val res = postBlockchain(blockchain)

      assertEquals(blockchain.address, res.address)
    }

  }
})