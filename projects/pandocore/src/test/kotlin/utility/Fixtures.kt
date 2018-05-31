package utility

import pando.Blockchain
import pando.Node
import pando.generateAddressPair
import java.security.PrivateKey

fun createNewBlockchain(): Pair<Blockchain, PrivateKey> {
  val pair = generateAddressPair()
  return Pair(pando.createNewBlockchain(pair.address, pair.keyPair.public, pair.keyPair.private), pair.keyPair.private)
}
