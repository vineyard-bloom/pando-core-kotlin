package serving

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import pando.*

fun createServer(source: BlockchainSource): ApplicationEngine{
  return embeddedServer(CIO, port = 8080){
    routing {
      get("/"){
        call.respondText("hey")
      }
      get("/address/{address}"){
        println(source(call.parameters["address"]!!)!!.address)
        if (source(call.parameters["address"]!!)!!.address == call.parameters["address"]) {
          call.respondText("Blockchain: ${source(call.parameters["address"]!!)}")
        }
        else {
          call.respondText("No Blockchain at address: ${call.parameters["address"]}")
        }
      }
    }
  }.start(wait = true)


}

fun main(args: Array<String>) {
  val pair = generateAddressPair()
  val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
  val source = { address:Address -> blockchain }

  createServer(source)
}

