package networking

import org.joda.time.DateTime
import pando.*

data class BlockchainData(
  val address: String,
  val publicKey: String,
  val blocks: List<BlockData>
)

data class BlockData(
  val hash: String,
  val index: Long,
  val address: String,
  val createdAt: DateTime
)

fun primitiveBlockchain(blockchain: Blockchain):BlockchainData {
  val primitiveBlockchain = BlockchainData(
    blockchain.address,
      keyToString(blockchain.publicKey),
    blockchain.blocks.map { BlockData(
      it!!.hash,
      it.index,
      it.address,
      it.createdAt
    ) }
  )
  return primitiveBlockchain
}