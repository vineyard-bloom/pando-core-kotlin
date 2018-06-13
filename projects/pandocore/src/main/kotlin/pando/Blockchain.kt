package pando

import java.security.PrivateKey
import java.security.PublicKey

data class Blockchain(
    val address: Address,
    val publicKey: PublicKey,
    val blocks: List<Block?>
)

fun createNewBlockchain(address: Address, publicKey: PublicKey) =
    Blockchain(address, publicKey, listOf())

fun getLastBlock(blockchain: Blockchain): Block? =
    if (blockchain.blocks.any())
      blockchain.blocks.last()
    else
      null

fun getBalance(blockchain: Blockchain): Long {
  var balance = 0L
  for (block in blockchain.blocks) {
    if (block!!.transaction.from == blockchain.address)
      balance -= block.transaction.value as Long
    else
      balance += block.transaction.value as Long
  }
  return balance
}

fun checkBalance(blockchain: Blockchain, block: Block): ValidationErrors {
  val balance = getBalance(blockchain)
  if (balance - block.transaction.value as Long >= 0L)
    return listOf()
  else
    return listOf(Error("Insufficient funds"))
}


fun mintTokens(blockchain: Blockchain, amount: TokenValue): Blockchain {
  val transaction = createTransaction(amount, blockchain.address, null)
  val pair = generateKeyPair()
  val newBlock = createBlock(blockchain, transaction, pair.private)
  return blockchain.copy(blocks = blockchain.blocks.plus(listOf(newBlock)))
}

fun sendTokens(fromBlockchain: Blockchain, toBlockchain: Blockchain, amount: TokenValue, privateKey: PrivateKey): List<Block> {
  val transaction = createTransaction(amount, toBlockchain.address, fromBlockchain.address)
  val fromBlock = createBlock(fromBlockchain, transaction, privateKey)
  val toBlock = createBlock(toBlockchain, transaction, privateKey)
  return listOf(fromBlock, toBlock)
}

fun addBlockWithoutValidation(blockchain: Blockchain, block: ValidatedBlock): Blockchain {
  return blockchain.copy(blocks = blockchain.blocks.plus(listOf(block.block)))
}

