package pando

interface Network {
  fun broadcastBlocks(node: Node, block: List<Block>)
}

class LocalNetwork(val nodes: List<Node>) : Network {

  override fun broadcastBlocks(node: Node, block: List<Block>) {
    val otherNodes = nodes.filter { it != node }
    // TODO find all related blockchains within each node and replace that blockchain with a new blockchain using addBlock.
  }

}