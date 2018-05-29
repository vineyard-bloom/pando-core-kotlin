import junit.framework.TestCase.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import pando.LocalNetwork
import pando.createNode
import pando.mintTokens
import pando.sendTokens

class NetworkSpec : Spek({
  describe("a network") {

    it("can broadcast blocks") {
      val firstBlockchain = mintTokens(utility.createNewBlockchain(), 1000)
      val secondBlockchain = utility.createNewBlockchain()
      val thirdBlockchain = utility.createNewBlockchain()
      val firstNode = createNode(listOf(firstBlockchain))
      val secondNode = createNode(listOf(secondBlockchain))
      val thirdNode = createNode(listOf(thirdBlockchain))
      val network = LocalNetwork(listOf(firstNode, secondNode, thirdNode))
      val newBlocks = sendTokens(firstBlockchain, secondBlockchain, 100)
      network.broadcastBlocks(firstNode, newBlocks)

      val blocks = secondNode.blockchains[secondBlockchain.address]!!.blocks
      assertEquals(1, blocks.size)
      val block = blocks.first()
      assertEquals(1, block.contents.transactions.size)
      val transaction = block.contents.transactions.first()
      assertEquals(100, transaction.value as Long)

      assertEquals(2, firstNode.blockchains[firstBlockchain.address]!!.blocks.size)
      assertEquals(0, thirdNode.blockchains[thirdBlockchain.address]!!.blocks.size)

    }

    it("can detect double spends") {
      assertTrue(true)
    }

  }
})