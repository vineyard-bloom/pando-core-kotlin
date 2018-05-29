import junit.framework.TestCase.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import pando.*

class ValidationSpec : Spek({
  describe("validation") {

    it("can detect good blockchains") {
      val genesis = mintTokens(utility.createNewBlockchain(), 1000)
      val pair = generateKeyPair()
      val transaction = createTransaction(100, "nowhere", genesis.address)
      val secondBlock = createBlock(genesis, listOf(signTransaction(transaction, pair.private)))
      val blockchain = addBlock(genesis, secondBlock)
//      val errors = validateBlockchain(blockchain)
//      assertEquals(0, errors.size)
    }

    it("can detect bad block hashes") {
      val genesis = mintTokens(utility.createNewBlockchain(), 1000)
      val pair = generateKeyPair()
      val transaction = createTransaction(100, "nowhere", genesis.address)
      val b = createBlock(genesis, listOf(signTransaction(transaction, pair.private)))
      val badBlock = Block(b.index, b.address, "Bad Hash", b.transactions, b.previousBlock, b.createdAt)
      val blockchain = addBlock(genesis, badBlock)
//      val errors = validateBlockchain(blockchain)
//      assertEquals(1, errors.size)
    }

  }
})