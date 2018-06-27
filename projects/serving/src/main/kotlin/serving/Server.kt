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
import jsoning.loadJsonFile
import jsoning.parseJson
import networking.BlockchainData
import networking.blockchainToPrimitve
import networking.primitiveToBlockchain
import pando.*
import persistence.AppConfig
import persistence.PandoDatabase
import java.util.concurrent.TimeUnit

data class Server(private val engine: ApplicationEngine) {

  fun stop(gracePeriod: Long, timeout: Long, timeUnit: TimeUnit) {
    engine.stop(gracePeriod, timeout, timeUnit)
  }
}

data class ServerConfig(
    val waiting: Boolean = true
)

fun createServer(source: BlockchainSource, consumer: BlockchainConsumer, config: ServerConfig = ServerConfig()): Server {

  val engine = embeddedServer(CIO, port = 8080) {
    routing {
      get("/") {
        call.respondText("hey")
      }
      get("/blockchain/{address}") {
        val blockchain = source(call.parameters["address"]!!)

        if (blockchain!!.address == call.parameters["address"]) {
          val primitiveBlockchain = blockchainToPrimitve(blockchain)

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
        consumer(blockchain)

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
fun loadAppConfig(path: String): AppConfig =
  loadJsonFile<AppConfig>(path)

fun initSources(): Pair<BlockchainSource, BlockchainConsumer> {
  val appConfig = loadAppConfig("config/config.json")
  val db = PandoDatabase(appConfig.database)
//  db.fixtureInit()
  val source = Pair({ address:Address -> db.loadBlockchain(address) }, { blockchain: Blockchain -> db.saveBlockchain(blockchain) })
  return source
}

fun main(args: Array<String>) {
  val source = initSources()

  createServer(source.first, source.second)
}
