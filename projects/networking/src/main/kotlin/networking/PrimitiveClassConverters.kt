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
  val primitiveBlockchain = BlockchainData(
    blockchain.address,
    keyToString(blockchain.publicKey),
    blockchain.blocks.map { BlockData(
      it!!.hash,
      BlockContentsData(
        it.contents.index,
        it.contents.address,
        BaseTransactionData(
          it.contents.transaction.hash,
          TransactionContentData(
            it.contents.transaction.value,
            it.contents.transaction.to,
            it.contents.transaction.from
          )
        ),
        it.previousBlock,
        it.createdAt
      ),
      signaturesToPrimitive(it.blockSignatures)
    )}
  )
  return primitiveBlockchain
}

fun signaturesToPrimitive(signatures: List<BlockSignature>):List<BlockSignatureData>{
  signatures.map
}



fun primitiveToBlockcain(blockchainData: BlockchainData):Blockchain {
  val blockchain = Blockchain(
          blockchainData.address,
          stringToPublicKey(blockchainData.publicKey),
          blockchainData.blocks.map { Block(
            it.hash,
            BlockContents(
              it.index,
              it.address,
              it.createdAt

            )
          ) }
  )
  return blockchain
}