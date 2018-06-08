package serving
import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.cio.CIO
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import jsoning.jsonify
import pando.*
import java.time.LocalDateTime

data class BlockchainData(
  val address: String,
  val publicKey: String,
  val blocks: List<BlockData>
)

data class BlockData(
  val hash: String,
  val index: Long,
  val address: String,
  val createdAt: LocalDateTime
)

fun createServer(source: BlockchainSource): ApplicationEngine {

  return embeddedServer(CIO, port = 8080) {
    routing {
      get("/") {
        call.respondText("hey")
      }
      get("/blockchain/{address}") {

        if (source(call.parameters["address"]!!)!!.address == call.parameters["address"]) {

          val primitiveBlockchain = BlockchainData(
            source(call.parameters["address"]!!)!!.address,
            source(call.parameters["address"]!!)!!.publicKey.toString(),
            source(call.parameters["address"]!!)!!.blocks.map { BlockData(
              it.hash,
              it.index,
              it.address,
              it.createdAt
            ) }
          )

          val json = jsonify<BlockchainData?>(primitiveBlockchain)
          call.respondText(json)

        } else {
          call.respondText("No blockchain found at address: ${call.parameters["address"]}")
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
