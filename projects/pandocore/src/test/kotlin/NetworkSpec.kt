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
      val firstNode = createNode(listOf(firstBlockchain, secondBlockchain))
      val spendA = sendTokens(firstBlockchain, secondBlockchain, 100, firstPrivateKey)

      val (validatedSpendAFrom, _) = validateBlock(spendA.first(), firstBlockchain.publicKey, firstBlockchain)
      assertNotNull(validatedSpendAFrom)

      val (validatedSpendATo, _) = validateBlock(spendA.last(), firstBlockchain.publicKey, firstBlockchain)
      assertNotNull(validatedSpendATo)
      addBlockToNode(firstNode, validatedSpendAFrom!!)
      addBlockToNode(firstNode, validatedSpendATo!!)

      val modifiedBlockchainOne = firstNode.blockchains[firstBlockchain.address]!!
      val modifiedBlockchainTwo = firstNode.blockchains[secondBlockchain.address]!!

      assertEquals(2, modifiedBlockchainOne.blocks.size)
      assertEquals(1, modifiedBlockchainTwo.blocks.size)
      assertEquals(0, thirdBlockchain.blocks.size)

      val balanceOne = getBalance(modifiedBlockchainOne)
      assertEquals(900, balanceOne)

      val balanceTwo = getBalance(modifiedBlockchainTwo)
      assertEquals(100, balanceTwo)

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