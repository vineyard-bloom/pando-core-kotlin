package pando

import java.time.LocalDateTime

typealias BlockIndex = Long

data class BlockContents(
    val index: BlockIndex,
    val address: Address,
    val transactions: List<BaseTransaction>,
    val previousBlock: Block?,
    val createdAt: LocalDateTime
)

data class Block(
    val hash: Hash,
    val contents: BlockContents
)

fun getBlockHash(block: Block?) =
    if (block != null)
      block.hash
    else
      null

fun createBlock(blockchain: Blockchain, transactions: List<BaseTransaction>): Block {
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
      hash = hash,
      contents = BlockContents(
        index = blockchain.blocks.size.toLong(),
        address = blockchain.address,
        transactions = transactions,
        previousBlock = previousBlock,
        createdAt = createdAt
      )
  )
}