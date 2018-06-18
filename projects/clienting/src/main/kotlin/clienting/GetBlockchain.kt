package clienting

import jsoning.jsonify
import jsoning.parseJson
import pando.Address
import networking.BlockchainData
import networking.primitiveBlockchain
import org.joda.time.DateTime
import pando.Blockchain
import java.io.BufferedWriter
import java.net.HttpURLConnection
import java.net.URL
import java.io.OutputStreamWriter
import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.post
import io.ktor.client.request.url
import kotlinx.coroutines.experimental.runBlocking
import io.ktor.http.ContentType
import io.ktor.http.contentType


fun getBlockchain(address: Address):BlockchainData {
  val res = URL("http://0.0.0.0:8080/blockchain/${address}").readText()
  val resBlock = parseJson<BlockchainData>(res)

  return resBlock
}

fun postBlockchain(blockchain: Blockchain) {
  runBlocking {
    val client = HttpClient(CIO)
    val primitiveBlockchain = primitiveBlockchain(blockchain)
    val json = jsonify<BlockchainData?>(primitiveBlockchain)
    val message = client.post<String> {
      url(URL("http://0.0.0.0:8080/blockchain/"))
//      contentType(ContentType.Application.Json)
//      body = json
    }

    println("CLIENT: Message from the server: $message")

  }

}
