package pando

import java.util.*

typealias BlockchainMap = Map<Address, Blockchain>

data class Node(
    val id: UUID,
    val blockchains: BlockchainMap
)

fun broadcastBlock(node: Node, block: Block) {

}