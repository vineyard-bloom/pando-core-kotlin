package serving

import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.cio.CIO
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import pando.Address
import pando.BlockchainSource
import pando.createNewBlockchain
import pando.generateAddressPair

fun createServer(source: BlockchainSource): ApplicationEngine {
  return embeddedServer(CIO, port = 8080) {
    routing {
      get("/") {
        call.respondText("hey")
      }
      get("/address/{address}") {
        println(source(call.parameters["address"]!!)!!.address)
        if (source(call.parameters["address"]!!)!!.address == call.parameters["address"]) {
          call.respondText("${source(call.parameters["address"]!!)}")
        } else {
          call.respondText("No Blockchain at address: ${call.parameters["address"]}")
        }
      }
    }
  }.start(wait = false)
}

fun main(args: Array<String>) {
  val pair = generateAddressPair()
  val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
  val source = { address: Address -> blockchain }

  createServer(source)
}

