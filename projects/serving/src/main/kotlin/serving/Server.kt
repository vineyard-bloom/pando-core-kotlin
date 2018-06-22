package serving

import io.ktor.application.ApplicationStarted
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.cio.CIO
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import jsoning.jsonify
import jsoning.parseJson
import networking.BlockchainData
import networking.blockchainToPrimitve
import networking.primitiveToBlockchain
import pando.*
import java.util.concurrent.TimeUnit

data class Server(private val engine: ApplicationEngine) {

  fun stop(gracePeriod: Long, timeout: Long, timeUnit: TimeUnit) {
    engine.stop(gracePeriod, timeout, timeUnit)
  }
}

data class ServerConfig(
    val waiting: Boolean = false
)

fun createServer(source: BlockchainSource, consumer: BlockchainConsumer, config: ServerConfig = ServerConfig()): Server {

  val engine = embeddedServer(CIO, port = 8080) {
    routing {
      get("/") {
        call.respondText("hey")
      }
      get("/blockchain/{address}") {

        if (source(call.parameters["address"]!!)!!.address == call.parameters["address"]) {

          val primitiveBlockchain = blockchainToPrimitve(source(call.parameters["address"]!!)!!)

          val json = jsonify<BlockchainData?>(primitiveBlockchain)
          call.respondText(json)

        } else {
          call.respondText("No blockchain found at address: ${call.parameters["address"]}")
        }
      }
      post("/blockchain") {
        val blockchainString = call.receiveText()
        val blockchainData = parseJson<BlockchainData>(blockchainString)
        val blockchain = primitiveToBlockchain(blockchainData)
        val res = consumer(blockchain)

        call.respond(HttpStatusCode.OK)
      }
    }
  }
  var listening = false
  engine.environment.monitor.subscribe(ApplicationStarted) { listening = true }

  engine.start(wait = config.waiting)
  while(!listening) {
    Thread.sleep(100)
  }

  return Server(engine)
}

fun main(args: Array<String>) {
  val pair = generateAddressPair()
  val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
  val source = { address: Address -> blockchain }
  val consumer = { blockchain: Blockchain -> Unit }

  createServer(source, consumer)
}
