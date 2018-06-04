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
      val transaction = createTransaction(100L, "nowhere", mining.address)
      val secondBlock = createBlock(mining, transaction, privateKey)
      val (_, errors) = validateBlock(secondBlock, mining.publicKey, mining)
      assertEquals(0, errors.size)
    }

    it("can detect bad block hashes") {
      val (genesis, privateKey) = utility.createNewBlockchain()
      val mining = mintTokens(genesis, 1000)
      val transaction = createTransaction(100L, "nowhere", mining.address)
      val b = createBlock(genesis, transaction, privateKey)
      val badBlock = Block("Bad Hash", BlockContents(b.contents.index, b.contents.address, b.contents.transaction, b.contents.previousBlock, b.contents.createdAt), b.blockSignatures)
      val (_, errors) = validateBlock(badBlock, genesis.publicKey, genesis)
      assertEquals(3, errors.size)
    }

    it("can detect bad block address") {
      val (genesis, privateKey) = utility.createNewBlockchain()
      val (secondBlockchain, privateKeyTwo) = utility.createNewBlockchain()
      val mining = mintTokens(genesis, 1000)
      val miningTwo = mintTokens(secondBlockchain, 1000)
      val transaction = createTransaction(100L, "nowhere", mining.address)
      val transactionTwo = createTransaction(100L, "nowhere", miningTwo.address)
      val b = createBlock(genesis, transaction, privateKey)
      val bTwo = createBlock(secondBlockchain, transactionTwo, privateKeyTwo)
      val badBlock = Block(b.hash, BlockContents(b.contents.index, bTwo.address, b.contents.transaction, b.contents.previousBlock, b.contents.createdAt), b.blockSignatures)
      val (_, errors) = validateBlock(badBlock, genesis.publicKey, genesis)
      assertEquals(2, errors.size)
    }

  }
})