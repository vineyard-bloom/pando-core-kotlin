package pando

data class Transaction(
    val hash: Hash,
    val value: Value,
    val to: Address,
    val from: Address
)

