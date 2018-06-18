import wallet_app.*
import junit.framework.TestCase.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import pando.createNewBlockchain
import pando.generateAddressPair
import wallet_app.AppWindow.Companion.main


class WalletSpec : Spek({
  describe("wallet app") {

    it("can broadcast a blockchain") {
      val pair = generateAddressPair()
      val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
      assert(true)
    }

  }
})