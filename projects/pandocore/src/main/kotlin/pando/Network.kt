package pando

interface Network {
  fun broadcastBlocks(node: Node, block: List<Block>)
}

class LocalNetwork(val nodes: List<Node>) : Network {

  override fun broadcastBlocks(node: Node, blocks: List<Block>) {
    val otherNodes = nodes.filter { it != node }
    val toAddress = blocks[0].transactions[0].to
    println(otherNodes[0])
    for (block in blocks) {
      otherNodes.flatMap { node -> node.blockchains.filter { it.key == block.address }.map { Pair(node, it) }}
              .forEach { it.first.blockchains[it.second.key] = addBlock()}
    }


    // TODO find all related blockchains within each node and replace that blockchain with a new blockchain using addBlock.
  }

}