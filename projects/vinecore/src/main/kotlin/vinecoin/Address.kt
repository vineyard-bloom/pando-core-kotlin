package vinecoin

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom

typealias Address = String

data class AddressKeyPair(
    val address: Address,
    val keyPair: KeyPair
)

fun generateAddressPair(): AddressKeyPair {
  val keyGen = KeyPairGenerator.getInstance("RSA", "SUN")
  val random = SecureRandom.getInstance("SHA1PRNG", "SUN")
  keyGen.initialize(1024, random)
  val pair = keyGen.generateKeyPair()
  val address = hash256(pair.public).substring(0, 40)
  return AddressKeyPair(privateKey, publicKey, address)
}