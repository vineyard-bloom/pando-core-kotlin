package pando

import org.apache.commons.codec.digest.DigestUtils
import java.nio.charset.Charset
import java.security.*
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import java.util.Base64.getDecoder
import java.io.IOException
import java.security.GeneralSecurityException
//import com.sun.tools.corba.se.idl.InterfaceState.Public
import java.security.PublicKey
import java.security.spec.EncodedKeySpec
import java.security.PrivateKey

fun generateRandomString(size: Int): String {
  val array = ByteArray(size)
  Random().nextBytes(array)
  return String(array, Charset.forName("UTF-8"))
}

fun hash256(privateKey: String): String {
  return DigestUtils.sha256Hex(privateKey)
}

fun keyToString(publicKey: PublicKey): String = Base64.getEncoder().encodeToString(publicKey.encoded)
fun privateKeyToString(privateKey: PrivateKey): String = Base64.getEncoder().encodeToString(privateKey.encoded)

fun stringToPublicKey(value: String): PublicKey {
  val bytes = Base64.getDecoder().decode(value)
  val keySpec = X509EncodedKeySpec(bytes)
  val keyFactory = KeyFactory.getInstance("RSA")
  return keyFactory.generatePublic(keySpec)
}

fun stringToPrivateKey(value: String): PrivateKey {
  val bytes = Base64.getDecoder().decode(value)
  val keySpec = PKCS8EncodedKeySpec(bytes)
  val keyFactory = KeyFactory.getInstance("RSA")
  return keyFactory.generatePrivate(keySpec)
}

fun byteArrayToString(signature: ByteArray): String = String(signature, Charset.forName("UTF-8"))

data class AddressKeyPair(
    val address: Address,
    val keyPair: KeyPair
)

fun generateKeyPair(): KeyPair {
  val keyGen = KeyPairGenerator.getInstance("RSA")
  val random = SecureRandom.getInstance("SHA1PRNG", "SUN")
  keyGen.initialize(1024, random)
  return keyGen.generateKeyPair()
}

fun getKeyPairAddress(pair: KeyPair): String = hash256(keyToString(pair.public)).substring(0, 40)

fun generateAddressPair(): AddressKeyPair {
  val pair = generateKeyPair()
  val address = getKeyPairAddress(pair)
  return AddressKeyPair(address, pair)
}

fun sign(privateKey: PrivateKey, data: String): ByteArray {
  val privateSignature = Signature.getInstance("SHA256withRSA")
  privateSignature.initSign(privateKey)
  privateSignature.update(data.toByteArray())
  return privateSignature.sign()
}

fun verify(data: String, signature: ByteArray, publicKey: PublicKey): Boolean {
  val publicSignature = Signature.getInstance("SHA256withRSA")
  publicSignature.initVerify(publicKey)
  publicSignature.update(data.toByteArray())
  return publicSignature.verify(signature)
}