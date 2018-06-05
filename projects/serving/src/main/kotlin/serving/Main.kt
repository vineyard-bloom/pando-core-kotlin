package serving

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import pando.*

fun createServer(source: BlockchainSource): NettyApplicationEngine{
  return embeddedServer(Netty, port = 8080){
    routing {
      get("/address/{address}"){
        println(source(call.parameters["address"]!!))
        if (source(call.parameters["address"]!!)!!.address == call.parameters["address"]) {
          call.respondText("Blockchain: ${source(call.parameters["address"]!!)}")
        }
        else {
          call.respondText("No blockchain found at that address")
        }
      }
    }
  }.start(wait = true)


}

fun main(args: Array<String>) {
  createServer({null})
}

