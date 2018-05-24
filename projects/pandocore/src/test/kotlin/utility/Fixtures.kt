package utility

import pando.Blockchain
import pando.Node
import pando.generateAddressPair

fun createNewBlockchain(): Blockchain {
  val pair = generateAddressPair()
  return pando.createNewBlockchain(pair.address)
}
