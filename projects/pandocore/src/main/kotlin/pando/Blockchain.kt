package pando

data class Blockchain(
    val address: Address,
    val blocks: List<Block>
)

fun createNewBlockchain(address: Address) =
    Blockchain(address, listOf())

fun mintTokens(blockchain: Blockchain, amount: TokenValue): Blockchain {
  return Blockchain(blockchain.address, blockchain.blocks)
}