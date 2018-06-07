package serving

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.cio.CIO
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.jackson.*
import io.ktor.features.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import pando.*

fun createServer(source: BlockchainSource): ApplicationEngine {

  return embeddedServer(CIO, port = 8080) {
    install(ContentNegotiation) {
      jackson {
        register(ContentType.Application.Json, JacksonConverter())
      }
    }
    routing {
      get("/") {
        call.respondText("hey")
      }
      get("/blockchain/{address}") {
        println(source(call.parameters["address"]!!)!!.address)
        if (source(call.parameters["address"]!!)!!.address == call.parameters["address"]) {
          call.respondText("${source(call.parameters["address"]!!)}", ContentType.Application.Json)
        } else {
          call.respondText("No blockchain found at address: ${call.parameters["address"]}")
        }
      }
    }
  }.start(wait = true)
}

fun main(args: Array<String>) {
  val pair = generateAddressPair()
  val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
  val source = { address: Address -> blockchain }

  createServer(source)
}
