package pando

typealias ValidationErrors = List<Error>

fun validateBlockchain(blockchain: Blockchain): ValidationErrors {
  return blockchain.blocks.flatMap { block ->
    if (hashBlock(BlockHashContents(
                    address = block.address,
                    valueType = ValueType.long,
                    transactionHashes = block.transactions.map { it.hash },
                    previousBlock = getBlockHash(block.previousBlock),
                    createdAt = block.createdAt
            )) == block.hash)
      listOf()
    else
      listOf(Error("Incorrect hash has been found in block " + block.index))
  }
}
