package pando

import java.time.LocalDateTime

data class Blockchain(
  val address: Address,
  val blocks: List<Block>
)

fun createNewBlockchain(address: Address) =
    Blockchain(address, listOf())

fun mintTokens(blockchain: Blockchain, amount: TokenValue): Blockchain {
  val transaction = Transaction("", amount, blockchain.address, null)
  val newBlock = Block(1, transaction.hash, listOf(transaction), "", LocalDateTime.now())
  return Blockchain(blockchain.address, blockchain.blocks.plus(listOf(newBlock)))
}
