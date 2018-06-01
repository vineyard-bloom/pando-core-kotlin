import junit.framework.TestCase.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import pando.*

class NetworkSpec : Spek({
  describe("a network") {

    it("can broadcast blocks") {
      val (genesisBlockchain, firstPrivateKey) = utility.createNewBlockchain()
      val firstBlockchain = mintTokens(genesisBlockchain, 100)
      val (secondBlockchain) = utility.createNewBlockchain()
      val (thirdBlockchain) = utility.createNewBlockchain()
      val firstNode = createNode(listOf(firstBlockchain))
      val spend = sendTokens(firstBlockchain, secondBlockchain, 100, firstPrivateKey)

      val (validatedSpendFrom, _) = validateBlock(spend.first(), firstBlockchain.publicKey, firstBlockchain)
      assertNotNull(validatedSpendFrom)

      val (validatedSpendTo, _) = validateBlock(spend.last(), firstBlockchain.publicKey, firstNode.blockchains[firstBlockchain.address]!!)
      assertNotNull(validatedSpendTo)

      addBlockToNode(firstNode, validatedSpendFrom!!)

      val modifiedBlockchain = firstNode.blockchains[firstBlockchain.address]!!

//      val blocks = secondBlockchain.blocks
//      assertEquals(1, blocks.size)
//      val block = blocks.first()
//      assertEquals(1, block.contents.transactions.size)
//      val transaction = block.contents.transactions.first()
//      assertEquals(100, transaction.value as Long)

      assertEquals(2, firstBlockchain.blocks.size)
      assertEquals(0, thirdBlockchain.blocks.size)
    }

    it("can detect double spends") {
      val (genesisBlockchain, firstPrivateKey) = utility.createNewBlockchain()
      val firstBlockchain = mintTokens(genesisBlockchain, 100)
      val (secondBlockchain) = utility.createNewBlockchain()
      val (thirdBlockchain) = utility.createNewBlockchain()
      val firstNode = createNode(listOf(firstBlockchain))
      val spendA = sendTokens(firstBlockchain, secondBlockchain, 100, firstPrivateKey)
      val spendB = sendTokens(firstBlockchain, thirdBlockchain, 100, firstPrivateKey)

      val (validatedSpendAFrom, _) = validateBlock(spendA.first(), firstBlockchain.publicKey, firstBlockchain)
      assertNotNull(validatedSpendAFrom)

      val (validatedSpendATo, _) = validateBlock(spendA.last(), firstBlockchain.publicKey, firstNode.blockchains[firstBlockchain.address]!!)
      assertNotNull(validatedSpendATo)
      addBlockToNode(firstNode, validatedSpendAFrom!!)
      val modifiedBlockchain = firstNode.blockchains[firstBlockchain.address]!!

      assertEquals(2, modifiedBlockchain.blocks.size)

      val balance = getBalance(modifiedBlockchain)
      assertEquals(0, balance)

      val (validatedSpendBFrom, _) = validateBlock(spendB.first(), firstBlockchain.publicKey, modifiedBlockchain)
      val (validatedSpendBTo, _) = validateBlock(spendB.last(), firstBlockchain.publicKey, modifiedBlockchain)
      assertNull(validatedSpendBFrom)
      assertNull(validatedSpendBTo)
//      addBlockToNode(firstNode, validatedSpendB!!)

    }

  }
})