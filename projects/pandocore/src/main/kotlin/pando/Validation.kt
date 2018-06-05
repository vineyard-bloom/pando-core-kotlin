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
  if (block.blockSignatures.isEmpty())
    listOf(Error("Block has no signatures"))
  else if (block.blockSignatures.all { !verify(block.hash, it.signature, publicKey)})
    listOf(Error("Invalid block signatures"))
  else
    listOf()

fun validateBlockAddress(block: Block, blockchain: Blockchain): ValidationErrors =
  if (block.blockSignatures.any { it.signer == block.address})
    listOf()
  else
    listOf(Error("Invalid block address"))

fun validateBlock(block: Block, publicKey: PublicKey, blockchain: Blockchain): Pair<ValidatedBlock?, ValidationErrors> {
  val hashErrors = validateBlockHash(block)
  val addressErrors = validateBlockAddress(block, blockchain)
  val blockSignatureErrors = validateBlockSignature(block, publicKey)
  val balanceErrors = checkBalance(blockchain, block)
  val errors = blockSignatureErrors.plus(balanceErrors).plus(hashErrors).plus(addressErrors)
  val validatedBlock = if (errors.none())
    ValidatedBlock(block)
  else
    null

  return Pair(validatedBlock, errors)
}

fun validateBlockchain(blockchain: Blockchain): ValidationErrors {
  return blockchain.blocks.flatMap { validateBlock(it, blockchain.publicKey, blockchain).second }
}
