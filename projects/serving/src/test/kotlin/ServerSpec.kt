import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import serving.*
import pando.*
import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.config
import io.ktor.client.call.*
import io.ktor.client.request.get
import io.ktor.client.response.readText
import junit.framework.TestCase.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import org.jetbrains.spek.api.dsl.on
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