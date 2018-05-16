package vinecoin

typealias Address = String
typealias Hash = String
typealias Value = Long

data class BlockBody(
    val transactions: List<Transaction>
)

data class Block(
    val hash: Hash,
    val transactions: List<Transaction>
)

data class Transaction(
    val hash: Hash,
    val value: Value,
    val to: Address,
    val from: Address
)