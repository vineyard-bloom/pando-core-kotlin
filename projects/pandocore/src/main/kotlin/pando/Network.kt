package pando

fun addBlockToNode(node: Node, block: ValidatedBlock) {
  return node.blockchains.filter { it.key == block.block.contents.address }
      .map { Pair(node, it) }
      .forEach {
        it.first.blockchains[it.second.key] = addBlockWithoutValidation(it.first.blockchains[it.second.key]!!, block)
      }
}

fun addBlocksToNode(node: Node, blocks: List<ValidatedBlock>) =
    blocks.forEach { addBlockToNode(node, it) }

//interface Network {
//  fun broadcastBlocks(node: Node, block: List<Block>)
//}

//class LocalNetwork(val nodes: List<Node>) : Network {
//
//  override fun broadcastBlocks(node: Node, blocks: List<Block>) {
//    for (block in blocks) {
//      nodes.map { addBlockToNode(it, block) }
//    }
//
//
//  }
//
//}