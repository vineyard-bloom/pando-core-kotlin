import junit.framework.TestCase.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import pando.*
import serving.createServer
import java.net.URL
import java.util.concurrent.TimeUnit
import jsoning.parseJson


class ServerSpec : Spek({
  describe("server requests") {

    it("can get blockchain from address") {

      val pair = generateAddressPair()
      val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
      val source = { address: Address -> blockchain }

      val server = createServer(source)
      val res = URL("http://0.0.0.0:8080/blockchain/${blockchain.address}").readText()
      val resBlock = parseJson<Blockchain>(res)
      server.stop(1000, 30, TimeUnit.SECONDS) // Not needed but a nicety


      assertEquals(blockchain, resBlock)
    }

  }
})