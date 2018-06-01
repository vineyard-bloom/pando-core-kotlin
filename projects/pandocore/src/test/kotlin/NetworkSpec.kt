import junit.framework.TestCase.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import pando.*

class NetworkSpec : Spek({
  describe("a network") {

    it("can broadcast blocks") {
      val (genesisBlockchain, firstPrivateKey) = utility.createNewBlockchain()
      val firstBlockchain = mintTokens(genesisBlockchain, 1000)
      val (secondBlockchain) = utility.createNewBlockchain()
      val (thirdBlockchain) = utility.createNewBlockchain()
      val firstNode = createNode(listOf(firstBlockchain))
      val secondNode = createNode(listOf(secondBlockchain))
      val thirdNode = createNode(listOf(thirdBlockchain))
      val network = LocalNetwork(listOf(firstNode, secondNode, thirdNode))
      val newBlocks = sendTokens(firstBlockchain, secondBlockchain, 100, firstPrivateKey)
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
      val (genesisBlockchain, firstPrivateKey) = utility.createNewBlockchain()
      val firstBlockchain = mintTokens(genesisBlockchain, 100)
      val (secondBlockchain) = utility.createNewBlockchain()
      val (thirdBlockchain) = utility.createNewBlockchain()
      val firstNode = createNode(listOf(firstBlockchain, secondBlockchain, thirdBlockchain))
      val spendOne = sendTokens(firstBlockchain, secondBlockchain, 100, firstPrivateKey)
      val spendTwo = sendTokens(firstBlockchain, thirdBlockchain, 100, firstPrivateKey)

      addBlocksToNode(firstNode, spendOne)
      addBlocksToNode(firstNode, spendTwo)

      assertEquals(1, firstNode.blockchains[secondBlockchain.address]!!.blocks.size + firstNode.blockchains[thirdBlockchain.address]!!.blocks.size)

    }

  }
})