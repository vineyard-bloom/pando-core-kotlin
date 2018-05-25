package pando

interface Network {
  fun broadcastBlocks(node: Node, block: List<Block>)
}

class LocalNetwork(val nodes: List<Node>) : Network {

  override fun broadcastBlocks(node: Node, blocks: List<Block>) {
//    val otherNodes = nodes.filter { it != node }
    val toAddress = blocks[0].transactions[0].to
    for (block in blocks) {
      nodes.flatMap { node -> node.blockchains.filter { it.key == block.address }.map { Pair(node, it) }}
                .forEach { it.first.blockchains[it.second.key] = addBlock(it.first.blockchains[it.second.key]!! , block) }
    }


    // TODO find all related blockchains within each node and replace that blockchain with a new blockchain using addBlock.
  }

}