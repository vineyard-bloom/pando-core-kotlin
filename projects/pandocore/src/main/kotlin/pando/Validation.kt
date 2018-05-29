package pando

typealias ValidationErrors = List<Error>

fun validateBlockchain(blockchain: Blockchain): ValidationErrors {
  return blockchain.blocks.flatMap { block ->
    if (hashBlock(BlockHashContents(
            address = block.contents.address,
            valueType = ValueType.long,
            transactionHashes = block.contents.transactions.map { it.hash },
            previousBlock = getBlockHash(block.contents.previousBlock),
            createdAt = block.contents.createdAt
        )) == block.hash)
      listOf()
    else
      listOf(Error("Incorrect hash has been found in block " + block.contents.index))
  }
}
