package pando

data class Blockchain(
    val address: Address,
    val blocks: List<Block>
)

fun createNewBlockchain(address: Address) =
    Blockchain(address, listOf())

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
  return Blockchain(blockchain.address, blockchain.blocks.plus(listOf(newBlock)))
}

//data class TransactionBlocks(
//    val to: Block,
//    val from: Block
//)

fun sendTokens(fromBlockchain: Blockchain, toBlockchain: Blockchain, amount: TokenValue): List<Block> {
  val transaction = createTransaction(amount, toBlockchain.address, fromBlockchain.address)
  val pair = generateKeyPair()
  val signedTransaction = signTransaction(transaction, pair.private)
  val fromBlock = createBlock(fromBlockchain, listOf(signedTransaction))
  val toBlock = createBlock(toBlockchain, listOf(signedTransaction))
  return listOf(fromBlock, toBlock)
}

fun validateBlock(block: Block): Boolean {
  val validBlock = block.contents.transactions.filter {
    it.to !== null && it.from !== null && it.signatures == null
  }
  if (validBlock.isNotEmpty()) {
    return false
  }
    return true
}

fun addBlock(blockchain: Blockchain, block: Block): Blockchain {
  val validBlock = validateBlock(block)
  if (validBlock) {
    return Blockchain(blockchain.address, blockchain.blocks.plus(listOf(block)))
  }
  else {
    throw Error("Invalid signature")
  }
}
