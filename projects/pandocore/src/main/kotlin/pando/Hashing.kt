package pando

import org.joda.time.DateTime

data class BlockHashContents(
    val address: Address,
    val valueType: ValueType,
    val transactionHashes: Hash,
    val previousBlock: Hash?,
    val createdAt: DateTime
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
      contents.transactionHashes +
      contents.createdAt

  return hash256(input)
}
