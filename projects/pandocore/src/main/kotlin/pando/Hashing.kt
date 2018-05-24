package pando

import java.time.LocalDateTime

data class BlockHashContents(
    val address: Address,
    val valueType: ValueType,
    val transactionHashes: List<String>,
    val previousBlock: Hash?,
    val createdAt: LocalDateTime
)

fun hashTransaction(value: Value, to: Address, from: Address?): String {
  val input = to +
      from +
      value
  return hash256(input)
}

fun hashBlock(contents: BlockHashContents): String {
  val input = contents.address +
      contents.valueType +
      contents.previousBlock +
      contents.transactionHashes.joinToString() +
      contents.createdAt

  return hash256(input)
}
