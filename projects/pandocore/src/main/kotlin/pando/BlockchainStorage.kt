package pando

typealias BlockchainSource = (address: Address) -> Blockchain?

typealias BlockchainConsumer = (blockchain: Blockchain) -> Unit

