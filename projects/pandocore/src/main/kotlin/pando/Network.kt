package pando

fun addBlockToNode(node: Node, block: Block) {
  node.blockchains.filter { it.key == block.contents.address }.map { Pair(node, it)}
          .forEach {
            getBalance(it.first.blockchains[it.second.key]!!)
            it.first.blockchains[it.second.key] = addBlockWithValidation(it.first.blockchains[it.second.key]!!, block)
          }
}

fun addBlocksToNode(node: Node, blocks: List<Block>) {
  for (block in blocks) {
    addBlockToNode(node, block)
  }
}

interface Network {
  fun broadcastBlocks(node: Node, block: List<Block>)
}

class LocalNetwork(val nodes: List<Node>) : Network {

  override fun broadcastBlocks(node: Node, blocks: List<Block>) {
    for (block in blocks) {
      nodes.map { addBlockToNode(it, block) }
    }


    // TODO find all related blockchains within each node and replace that blockchain with a new blockchain using addBlockWithValidation.
  }

}