package pando

import java.security.PublicKey

typealias ValidationErrors = List<Error>

fun validateBlockHash(block: Block): ValidationErrors =
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

fun validateTransactionSignature(transaction: SignedTransaction, publicKey: PublicKey): Boolean =
  transaction.signatures.all { verify(transaction.hash, it, publicKey) }


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

fun validateBlock(block: Block, publicKey: PublicKey): ValidationErrors {
  val hashErrors = validateBlockHash(block)
  val transactionSignatureErrors = validateBlockTransactionSignatures(block, publicKey)
  return hashErrors.plus(transactionSignatureErrors)
}

fun validateBlockchain(blockchain: Blockchain): ValidationErrors {
  return blockchain.blocks.flatMap { validateBlock(it, blockchain.publicKey) }
}
