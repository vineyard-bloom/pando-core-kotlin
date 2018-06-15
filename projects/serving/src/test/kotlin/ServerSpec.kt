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
//import serving.recieveBlockchain



class ServerSpec : Spek({
  describe("server requests") {

    it("can get blockchain from address") {

      val pair = generateAddressPair()
      val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
      val source = { address: Address -> blockchain }
      val server = createServer(source)
      val res = getBlockchain(blockchain.address)
      server.stop(1000, 30, TimeUnit.SECONDS) // Not needed but a nicety


      assertEquals(blockchain.address, res.address)
    }

    it("can save a blockchain") {
      val pair = generateAddressPair()
      val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
      val source = { address: Address -> blockchain }
      val server = createServer(source)
//      recieveBlockchain(blockchain)
      server.stop(1000, 30, TimeUnit.SECONDS)
    }

  }
})