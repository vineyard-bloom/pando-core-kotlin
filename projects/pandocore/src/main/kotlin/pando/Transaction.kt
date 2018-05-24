package pando

data class Transaction(
    val hash: Hash,
    val value: Value,
    val to: Address,
    val from: Address?
)

fun createTransaction(value: Value, to: Address, from: Address?): Transaction {
  return Transaction(
      hashTransaction(value, to, from),
      value,
      to,
      from
  )
}
