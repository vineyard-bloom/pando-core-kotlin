package serving

import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.cio.CIO
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import jsoning.jsonify
import networking.BlockchainData
import networking.primitiveBlockchain
import pando.Address
import pando.BlockchainSource
import pando.createNewBlockchain
import pando.generateAddressPair
import java.util.concurrent.TimeUnit

data class Server(private val engine: ApplicationEngine) {

  fun stop(gracePeriod: Long, timeout: Long, timeUnit: TimeUnit) {
    engine.stop(gracePeriod, timeout, timeUnit)
  }
}

fun createServer(source: BlockchainSource): Server {

  val engine = embeddedServer(CIO, port = 8080) {
    routing {
      get("/") {
        call.respondText("hey")
      }
      get("/blockchain/{address}") {

        if (source(call.parameters["address"]!!)!!.address == call.parameters["address"]) {

          val primitiveBlockchain = primitiveBlockchain(source(call.parameters["address"]!!)!!)

          val json = jsonify<BlockchainData?>(primitiveBlockchain)
          call.respondText(json)

        } else {
          call.respondText("No blockchain found at address: ${call.parameters["address"]}")
        }
      }
    }
  }.start(wait = false)

  return Server(engine)
}

fun main(args: Array<String>) {
  val pair = generateAddressPair()
  val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
  val source = { address: Address -> blockchain }

  createServer(source)
}
