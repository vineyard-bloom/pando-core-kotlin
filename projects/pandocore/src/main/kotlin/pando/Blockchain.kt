package pando

import java.security.PublicKey

data class Blockchain(
    val address: Address,
    val publicKey: PublicKey,
    val blocks: List<Block>
)

fun createNewBlockchain(address: Address, publicKey: PublicKey) =
    Blockchain(address, publicKey, listOf())

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

fun sendTokens(fromBlockchain: Blockchain, toBlockchain: Blockchain, amount: TokenValue): List<Block> {
  val transaction = createTransaction(amount, toBlockchain.address, fromBlockchain.address)
  val pair = generateKeyPair()
  val signedTransaction = signTransaction(transaction, pair.private)
  val fromBlock = createBlock(fromBlockchain, listOf(signedTransaction))
  val toBlock = createBlock(toBlockchain, listOf(signedTransaction))
  return listOf(fromBlock, toBlock)
}

fun addBlock(blockchain: Blockchain, block: Block): Blockchain {
  val validationErrors = validateBlock(block, blockchain.publicKey)
  if (validationErrors.none()) {
    return blockchain.copy(blocks = blockchain.blocks.plus(listOf(block)))
  } else {
    throw validationErrors.first()
  }
}
