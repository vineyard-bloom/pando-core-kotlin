package pando

import java.security.PrivateKey
import java.security.PublicKey

data class Blockchain(
    val address: Address,
    val publicKey: PublicKey,
    val privateKey: PrivateKey,
    val blocks: List<Block>
)

fun createNewBlockchain(address: Address, publicKey: PublicKey, privateKey: PrivateKey) =
    Blockchain(address, publicKey, privateKey, listOf())

fun getLastBlock(blockchain: Blockchain): Block? =
    if (blockchain.blocks.any())
      blockchain.blocks.last()
    else
      null

fun mintTokens(blockchain: Blockchain, amount: TokenValue): Blockchain {
  val transaction = createTransaction(amount, blockchain.address, null)
  val pair = generateKeyPair()
  val signedTransaction = signTransaction(transaction, pair.private)
  val newBlock = createBlock(blockchain, listOf(signedTransaction))
  return blockchain.copy(blocks = blockchain.blocks.plus(listOf(newBlock)))
}

fun sendTokens(fromBlockchain: Blockchain, toBlockchain: Blockchain, amount: TokenValue, privateKey: PrivateKey): List<Block> {
  val transaction = createTransaction(amount, toBlockchain.address, fromBlockchain.address)
  val signedTransaction = signTransaction(transaction, privateKey)
  val fromBlock = createBlock(fromBlockchain, listOf(signedTransaction))
  val toBlock = createBlock(toBlockchain, listOf(signedTransaction))
  return listOf(fromBlock, toBlock)
}

fun addBlockWithValidation(blockchain: Blockchain, block: Block): Blockchain  {
  val validationErrors = validateBlock(block, blockchain.publicKey)
  if (validationErrors.none()) {
    return blockchain.copy(blocks = blockchain.blocks.plus(listOf(block)))
  } else {
    throw Error("Block hash is not valid")
  }
}

fun addBlockWithoutValidation(blockchain: Blockchain, block: Block): Blockchain  {
  return blockchain.copy(blocks = blockchain.blocks.plus(listOf(block)))
}

