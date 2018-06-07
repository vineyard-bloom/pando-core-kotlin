import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import pando.*
import junit.framework.TestCase.*
import java.net.URL


class ServerSpec : Spek({
  describe("server requests") {

    it("can get blockchain from address") {

      val pair = generateAddressPair()
      val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
      val res =  URL("http://0.0.0.0:8080/address/${blockchain.address}").readText()

      assertEquals(blockchain, res)
    }

  }
})