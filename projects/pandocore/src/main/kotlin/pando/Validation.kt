package pando

import java.security.PublicKey

typealias ValidationErrors = List<Error>

fun validateBlockHash(block: Block): ValidationErrors =
    if (hashBlock(BlockHashContents(
            address = block.contents.address,
            valueType = ValueType.long,
            transactionHashes = block.contents.transaction.hash,
            previousBlock = getBlockHash(block.contents.previousBlock),
            createdAt = block.contents.createdAt
        )) == block.hash)
      listOf()
    else
      listOf(Error("Incorrect hash has been found in block " + block.contents.index))


fun validateBlockSignature(block: Block, publicKey: PublicKey): ValidationErrors =
  if (block.blockSignature.signature.isEmpty())
    listOf(Error("Block has no signatures"))
  else if (!verify(block.hash, block.blockSignature.signature, publicKey))
    listOf(Error("Invalid transaction signature."))
  else
    listOf()


fun validateBlock(block: Block, publicKey: PublicKey, blockchain: Blockchain): Pair<ValidatedBlock?, ValidationErrors> {
  val hashErrors = validateBlockHash(block)
  val blockSignatureErrors = validateBlockSignature(block, publicKey)
  val balanceErrors = checkBalance(blockchain, block)
  val errors = (balanceErrors)
  val validatedBlock = if (errors.none())
    ValidatedBlock(block)
  else
    null

  return Pair(validatedBlock, errors)
}

fun validateBlockchain(blockchain: Blockchain): ValidationErrors {
  return blockchain.blocks.flatMap { validateBlock(it, blockchain.publicKey, blockchain).second }
}
