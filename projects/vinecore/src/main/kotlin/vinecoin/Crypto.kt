package vinecoin

import org.apache.commons.codec.digest.DigestUtils
import java.nio.charset.Charset
import java.util.*

fun generateRandomString(size: Int): String {
  val array = ByteArray(size)
  Random().nextBytes(array)
  return String(array, Charset.forName("UTF-8"))
}

fun hash256(privateKey: String): String {
  return DigestUtils.sha256Hex(privateKey)
}