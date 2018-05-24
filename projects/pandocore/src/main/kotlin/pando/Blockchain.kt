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
  val newBlock = createBlock(blockchain, listOf(transaction))
  return Blockchain(blockchain.address, blockchain.blocks.plus(listOf(newBlock)))
}

//data class TransactionBlocks(
//    val to: Block,
//    val from: Block
//)

fun sendTokens(fromBlockchain: Blockchain, toBlockchain: Blockchain, amount: TokenValue): List<Block> {
  val transaction = createTransaction(amount, toBlockchain.address, fromBlockchain.address)
  val fromBlock = createBlock(fromBlockchain, listOf(transaction))
  val toBlock = createBlock(toBlockchain, listOf(transaction))
  return listOf(fromBlock, toBlock)
}

fun addBlock(blockchain: Blockchain, block: Block): Blockchain {
  return Blockchain(blockchain.address, blockchain.blocks.plus(listOf(block)))
}
