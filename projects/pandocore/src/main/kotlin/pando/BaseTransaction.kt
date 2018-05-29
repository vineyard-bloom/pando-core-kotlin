package pando

data class TransactionContent(
    val value: Value,
    val to: Address,
    val from: Address?
)

data class BaseTransaction(
    val hash: Hash,
    val content: TransactionContent
) {
  val value: Value
    get() = content.value

  val to: Address
    get() = content.to

  val from: Address?
    get() = content.from
}

data class SignedTransaction(
    val base: BaseTransaction,
    val signatures: List<String>
) {
  val hash: Hash
    get() = base.hash

  val content: TransactionContent
    get() = base.content
}

fun createTransaction(content: TransactionContent): BaseTransaction {
  return BaseTransaction(
      hashTransaction(content.value, content.to, content.from),
      content
  )
}

fun createTransaction(value: Value, to: Address, from: Address?) =
    createTransaction(TransactionContent(value, to, from))

fun signTransaction(transaction: BaseTransaction): SignedTransaction {
  throw Error("Not implemented.")
}
