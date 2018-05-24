import junit.framework.TestCase.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import pando.generateKeyPair
import pando.sign
import pando.verify

class CryptoSpec : Spek({
  describe("cryptography") {

    it("can detect valid signatures") {
      val pair = generateKeyPair()
      val data = "There is always an ogre behind you."
      val signature = sign(pair.private, data)
      val verified = verify(data, signature, pair.public)
      assertTrue(verified)
    }

    it("can detect invalid signatures") {
      val pair = generateKeyPair()
      val secondPair = generateKeyPair()
      val data = "There is always an ogre behind you."
      val signature = sign(secondPair.private, data)
      val verified = verify(data, signature, pair.public)
      assertFalse(verified)
    }
  }
})