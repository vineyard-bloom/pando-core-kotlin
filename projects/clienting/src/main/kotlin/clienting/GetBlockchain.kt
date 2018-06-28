package clienting

import jsoning.jsonify
import jsoning.parseJson
import pando.Address
import networking.BlockchainData
import networking.blockchainToPrimitve
import pando.Blockchain
import java.net.URL
import io.ktor.client.*
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.response.HttpResponse
import kotlinx.coroutines.experimental.runBlocking
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType


fun getBlockchain(url: String, address: Address):BlockchainData {
  val res = URL("$url/blockchain/$address").readText()

  return parseJson<BlockchainData>(res)
}

fun postBlockchain(blockchain: Blockchain):HttpStatusCode {
  val client = HttpClient(Apache)
  val primitiveBlockchain = blockchainToPrimitve(blockchain)
  val json = jsonify<BlockchainData?>(primitiveBlockchain)
  val res = runBlocking {

    client.post<HttpResponse> {
      url(URL("http://0.0.0.0:8080/blockchain/"))
      contentType(ContentType.Application.Json)
      body = json
    }
  }

  println("Response: ${res.status}")
  return res.status
}