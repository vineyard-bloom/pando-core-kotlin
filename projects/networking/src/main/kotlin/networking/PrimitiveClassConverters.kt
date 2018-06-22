package networking

import org.joda.time.DateTime
import pando.*
import java.security.PublicKey

data class BlockchainData(
  val address: String,
  val publicKey: String,
  val blocks: List<BlockData?>
)

data class BlockData(
  val hash: String,
  val contents: BlockContentsData,
  val blockSignatures: List <BlockSignatureData>
)

data class BlockContentsData(
  val index: Long,
  val address: String,
  val transaction: BaseTransactionData,
  val previousBlock: Hash?,
  val createdAt: DateTime
)

data class BlockSignatureData(
  val signer: String,
  val publicKey: String,
  val signature: String
)


data class BaseTransactionData(
  val hash: String,
  val content: TransactionContentData
)

data class TransactionContentData(
  val value: Value,
  val to: String,
  val from: String?
)

fun blockchainToPrimitve(blockchain: Blockchain):BlockchainData {
  return BlockchainData(
    blockchain.address,
    keyToString(blockchain.publicKey),
    blockchain.blocks.map { blockToPrimitive(it) }
  )
}

fun blockToPrimitive(block: Block?): BlockData {
  return BlockData(
      block!!.hash,
      BlockContentsData(
        block.contents.index,
        block.contents.address,
        BaseTransactionData(
          block.contents.transaction.hash,
          TransactionContentData(
            block.contents.transaction.value,
            block.contents.transaction.to,
            block.contents.transaction.from
          )
        ),
        block.previousBlock,
        block.createdAt
      ),
      signaturesToPrimitive(block.blockSignatures)
    )
}

fun signaturesToPrimitive(signatures: List<BlockSignature>):List<BlockSignatureData>{
  return signatures.map {
    BlockSignatureData(
      it.signer,
      keyToString(it.publicKey),
      byteArrayToString(it.signature)
    )
  }

}


fun primitiveToBlockchain(blockchain: BlockchainData):Blockchain {
  return Blockchain(
    blockchain.address,
    stringToPublicKey(blockchain.publicKey),
    blockchain.blocks.map { primitiveToBlock(it) }
  )
}

fun primitiveToBlock(block: BlockData?): Block {
  return Block(
    block!!.hash,
    BlockContents(
      block.contents.index,
      block.contents.address,
      BaseTransaction(
        block.contents.transaction.hash,
        TransactionContent(
          block.contents.transaction.content.value,
          block.contents.transaction.content.to,
          block.contents.transaction.content.from
        )
      ),
      block.contents.previousBlock,
      block.contents.createdAt
    ),
    primitiveToSignature(block.blockSignatures)
  )
}

fun primitiveToSignature(signatures: List<BlockSignatureData>):List<BlockSignature>{
  return signatures.map {
    BlockSignature(
      it.signer,
      stringToPublicKey(it.publicKey),
      it.signature.toByteArray()
    )
  }

}
