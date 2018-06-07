import junit.framework.TestCase.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import pando.createNewBlockchain
import pando.generateAddressPair
import serving.createServer
import java.net.URL
import java.util.concurrent.TimeUnit


class ServerSpec : Spek({
  describe("server requests") {

    it("can get blockchain from address") {

      val pair = generateAddressPair()
      val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
      val blockchains = mapOf(
          blockchain.address to blockchain
      )

      val server = createServer({ blockchains[it] })
      val res = URL("http://0.0.0.0:8080/address/${blockchain.address}").readText()
      server.stop(1000, 30, TimeUnit.SECONDS) // Not needed but a nicety
      assertEquals(blockchain, res)
    }

  }
})