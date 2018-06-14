package pando

import org.joda.time.DateTime
import java.security.PrivateKey
import java.security.PublicKey

typealias BlockIndex = Long

data class BlockContents(
    val index: BlockIndex,
    val address: Address,
    val transaction: BaseTransaction,
    val previousBlock: Block?,
    val createdAt: DateTime
)

data class BlockSignature(
  val signer: Address,
  val publicKey: PublicKey,
  val signature: ByteArray
)

data class Block(
    val hash: Hash,
    val contents: BlockContents,
    val blockSignatures: List <BlockSignature>
) {
  val index: BlockIndex get() = contents.index
  val address: Address get() = contents.address
  val transaction: BaseTransaction get() = contents.transaction
  val previousBlock: Block? get() = contents.previousBlock
  val createdAt: DateTime get() = contents.createdAt
}

data class ValidatedBlock(
    val block: Block
)

fun getBlockHash(block: Block?) =
    if (block != null)
      block.hash
    else
      null

fun createBlock(blockchain: Blockchain, transaction: BaseTransaction, privateKey: PrivateKey): Block {
  val previousBlock = getLastBlock(blockchain)
  val createdAt = DateTime.now()
  val hash = hashBlock(BlockHashContents(
      address = blockchain.address,
      valueType = ValueType.long, // blockchain.valueType,
      transactionHashes = transaction.hash,
      previousBlock = getBlockHash(previousBlock),
      createdAt = createdAt
  ))
  val signature = signBlock(privateKey, hash)
  val blockSignature = BlockSignature(
    signer = blockchain.address,
    publicKey = blockchain.publicKey,
    signature = signature
  )

  return Block(
      hash = hash,
      blockSignatures = listOf(blockSignature),
      contents = BlockContents(
          index = blockchain.blocks.size.toLong(),
          address = blockchain.address,
          transaction = transaction,
          previousBlock = previousBlock,
          createdAt = createdAt
      )
  )
}

fun signBlock(privateKey: PrivateKey, blockHash: Hash): ByteArray =
  sign(privateKey, blockHash)
