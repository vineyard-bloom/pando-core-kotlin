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

//fun validateTransactionSignature(transaction: BaseTransaction, publicKey: PublicKey): Boolean =
//    transaction.signatures.all { verify(transaction.hash, it, publicKey) }


fun validateBlockTransactionSignatures(block: Block, publicKey: PublicKey): ValidationErrors =
    block.transactions
        .filter { it.from == block.address }
        .flatMap {
          if (it.signatures.none())
            listOf(Error("Transaction has no signatures"))
          else if (!validateTransactionSignature(it, publicKey))
            listOf(Error("Invalid transaction signature."))
          else
            listOf()
        }

fun validateBlock(block: Block, publicKey: PublicKey, blockchain: Blockchain): Pair<ValidatedBlock?, ValidationErrors> {
  val hashErrors = validateBlockHash(block)
  val transactionSignatureErrors = validateBlockTransactionSignatures(block, publicKey)
  val balanceErrors = checkBalance(blockchain, block)
  val errors = hashErrors.plus(transactionSignatureErrors).plus(balanceErrors)
  val validatedBlock = if (errors.none())
    ValidatedBlock(block)
  else
    null

  return Pair(validatedBlock, errors)
}

fun validateBlockchain(blockchain: Blockchain): ValidationErrors {
  return blockchain.blocks.flatMap { validateBlock(it, blockchain.publicKey, blockchain).second }
}
