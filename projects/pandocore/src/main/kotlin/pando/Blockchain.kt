package pando

import java.security.PrivateKey
import java.security.PublicKey

data class Blockchain(
    val address: Address,
    val publicKey: PublicKey,
    val blocks: List<Block>
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
    if (block.transactions.first().from == blockchain.address)
      balance -= block.transactions.first().value as Long
    else
      balance += block.transactions.first().value as Long
  }
  return balance
}

fun checkBalance(blockchain: Blockchain, block: Block): ValidationErrors {
  val balance = getBalance(blockchain)
  if (balance - block.transactions.first().value as Long >= 0)
    return listOf()
  else
    return listOf(Error("Insufficient funds"))
}


fun mintTokens(blockchain: Blockchain, amount: TokenValue): Blockchain {
  val transaction = createTransaction(amount, blockchain.address, null)
  val pair = generateKeyPair()
  val signedTransaction = signTransaction(transaction, pair.private)
  val newBlock = createBlock(blockchain, listOf(signedTransaction))
  return blockchain.copy(blocks = blockchain.blocks.plus(listOf(newBlock)))
}

fun sendTokens(fromBlockchain: Blockchain, toBlockchain: Blockchain, amount: TokenValue, privateKey: PrivateKey): List<Block> {
  val transaction = createTransaction(amount, toBlockchain.address, fromBlockchain.address)
  val signedTransaction = signTransaction(transaction, privateKey)
  val fromBlock = createBlock(fromBlockchain, listOf(signedTransaction))
  val toBlock = createBlock(toBlockchain, listOf(signedTransaction))
  val balanceErrors = checkBalance(fromBlockchain, fromBlock)
  return listOf(fromBlock, toBlock)
}

fun addBlockWithValidation(blockchain: Blockchain, block: Block): Blockchain  {
  val validationErrors = validateBlock(block, blockchain.publicKey, blockchain)
  if (validationErrors.none()) {
    return blockchain.copy(blocks = blockchain.blocks.plus(listOf(block)))
  }
  else {
    return blockchain
//    throw validationErrors.first()
  }
}

fun addBlockWithoutValidation(blockchain: Blockchain, block: Block): Blockchain  {
  return blockchain.copy(blocks = blockchain.blocks.plus(listOf(block)))
}

