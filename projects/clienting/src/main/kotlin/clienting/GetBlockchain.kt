package clienting

import jsoning.parseJson
import pando.Address
import networking.BlockchainData
import java.net.URL

fun getBlockchain(address: Address):BlockchainData {
  val res = URL("http://0.0.0.0:8080/blockchain/${address}").readText()
  val resBlock = parseJson<BlockchainData>(res)

  return resBlock
}