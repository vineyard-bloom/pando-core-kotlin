package pando

import java.time.LocalDateTime

typealias BlockIndex = Long

data class ProposedBlock(
    val transactions: List<Transaction>
)

data class Block(
    val index: BlockIndex,
    val hash: Hash,
    val transactions: List<Transaction>,
    val previousBlock: Hash,
    val createdAt: LocalDateTime
)