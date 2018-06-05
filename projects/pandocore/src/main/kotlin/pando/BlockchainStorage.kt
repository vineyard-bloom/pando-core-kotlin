package pando

interface BlockchainStorage {
  fun loadBlockchain(address: Address): Blockchain?
  fun saveBlockchain(blockchain: Blockchain)
}