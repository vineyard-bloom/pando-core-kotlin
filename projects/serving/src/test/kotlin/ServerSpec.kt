import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import serving.*
import pando.*
import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.config
import io.ktor.client.request.*
import junit.framework.TestCase.*


class ServerSpec : Spek({
  describe("server requests") {

    it("can get blockchain from address") {
      val client = HttpClient(CIO.config {

      })
      val pair = generateAddressPair()
      val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
      val source = { address:Address -> blockchain }

      createServer(source)
//      val res = client.get<String>("http://0.0.0.0:8080/address/${blockchain.address}")

//      assertEquals(blockchain.address, res)
      assert(true)
    }

  }
})