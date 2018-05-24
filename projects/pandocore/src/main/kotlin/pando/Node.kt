package pando

import java.util.*

typealias BlockchainMap = MutableMap<Address, Blockchain>

data class Node(
    val id: UUID,
    val blockchains: BlockchainMap
)

fun createNode(blockchains: List<Blockchain>) =
    Node(UUID.randomUUID(), blockchains.associate { Pair(it.address, it) }.toMutableMap())