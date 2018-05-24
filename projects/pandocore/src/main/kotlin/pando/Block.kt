package pando

import java.time.LocalDateTime

typealias BlockIndex = Long

data class Block(
    val index: BlockIndex,
    val hash: Hash,
    val transactions: List<Transaction>,
    val previousBlock: Block?,
    val createdAt: LocalDateTime
)

fun createBlock(blockchain: Blockchain, transactions: List<Transaction>): Block {
  val previousBlock = blockchain.blocks.last()
  val createdAt = LocalDateTime.now()
  val hash = hashBlock(BlockHashContents(
      address = blockchain.address,
      valueType = ValueType.long, // blockchain.valueType,
      transactionHashes = transactions.map { it.hash },
      previousBlock = previousBlock.hash,
      createdAt = createdAt
  ))

  return Block(
      index = blockchain.blocks.size.toLong(),
      hash = hash,
      transactions = transactions,
      previousBlock = previousBlock,
      createdAt = createdAt
  )
}