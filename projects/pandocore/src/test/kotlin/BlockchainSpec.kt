import junit.framework.TestCase.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import pando.*
import utility.createNewBlockchain

class BlockchainSpec : Spek({
  describe("blockchains") {

    it("can mint new tokens") {
      val newBlockchain =  createNewBlockchain()
      val updatedBlockchain = mintTokens(newBlockchain, 1000)
      val blocks = updatedBlockchain.blocks
      assertEquals(1, blocks.size)
      val block = blocks.first()
      assertEquals(1, block.transactions.size)
      val transaction = block.transactions.first()
      assertEquals(1000, transaction.value as Long)
      assertNull(transaction.from)
      assertEquals(newBlockchain.address, transaction.to)
    }

  }
})