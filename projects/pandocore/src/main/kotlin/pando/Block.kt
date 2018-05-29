package pando

import java.time.LocalDateTime

typealias BlockIndex = Long

data class Block(
    val index: BlockIndex,
    val address: Address,
    val hash: Hash,
    val transactions: List<SignedTransaction>,
    val previousBlock: Block?,
    val createdAt: LocalDateTime
)

fun getBlockHash(block: Block?) =
    if (block != null)
      block.hash
    else
      null

fun createBlock(blockchain: Blockchain, transactions: List<SignedTransaction>): Block {
  val previousBlock = getLastBlock(blockchain)
  val createdAt = LocalDateTime.now()
  val hash = hashBlock(BlockHashContents(
      address = blockchain.address,
      valueType = ValueType.long, // blockchain.valueType,
      transactionHashes = transactions.map { it.hash },
      previousBlock = getBlockHash(previousBlock),
      createdAt = createdAt
  ))

  return Block(
      index = blockchain.blocks.size.toLong(),
      address = blockchain.address,
      hash = hash,
      transactions = transactions,
      previousBlock = previousBlock,
      createdAt = createdAt
  )
}