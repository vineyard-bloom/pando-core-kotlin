import junit.framework.TestCase.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import pando.*

class ValidationSpec : Spek({
  describe("validation") {

    it("can detect good blockchains") {
      val (genesis, privateKey) = utility.createNewBlockchain()
      val mining = mintTokens(genesis, 1000)
      val transaction = createTransaction(100, "nowhere", mining.address)
      val secondBlock = createBlock(mining, listOf(signTransaction(transaction, privateKey)), privateKey)
      val (_, errors) = validateBlock(secondBlock, mining.publicKey, mining)
      assertEquals(0, errors.size)
    }

    it("can detect bad block hashes") {
      val (genesis, privateKey) = utility.createNewBlockchain()
      val mining = mintTokens(genesis, 1000)
      val transaction = createTransaction(100, "nowhere", mining.address)
      val b = createBlock(genesis, listOf(signTransaction(transaction, privateKey)), privateKey)
      val badBlock = Block("Bad Hash", BlockContents(b.contents.index, b.contents.address, b.contents.transactions, b.contents.previousBlock, b.contents.createdAt), b.blockSignature)
      val (blockchain, errors) = validateBlock(badBlock, genesis.publicKey, genesis)
      assertEquals(1, errors.size)
    }

  }
})