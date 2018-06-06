import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import serving.*
import pando.*
import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import junit.framework.TestCase.*
import java.net.URL


class ServerSpec : Spek({
  describe("server requests") {

    it("can get blockchain from address") {
      val client = HttpClient(CIO)
      val pair = generateAddressPair()
      val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)

      val test =  URL("http://0.0.0.0:8080/address/${blockchain.address}").readText()
      assertEquals(blockchain, test)


    }

  }
})