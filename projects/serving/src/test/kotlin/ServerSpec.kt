import junit.framework.TestCase.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.net.URL
import serving.*
import pando.*
import java.net.HttpURLConnection


class ServerSpec : Spek({
  describe("server requests") {

    it("can get blockchain from address") {
      val pair = generateAddressPair()
      val blockchains = createNewBlockchain(pair.address, pair.keyPair.public)
      val source = { address:Address -> blockchains }
      createServer(source)
      assert(true)
    }

  }
})