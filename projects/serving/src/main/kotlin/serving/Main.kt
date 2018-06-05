package serving

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import pando.BlockchainSource

fun main(args: Array<String>) {
  embeddedServer(Netty, port = 8080, module = Application::mainModule).start(wait = true)
}

fun Application.mainModule() {

  routing {
    root()
    address()
  }

}

fun Routing.root() {
  get("/") {
    call.respondText("Nothing to see here")
  }
}

fun Routing.address() {
  get("/address/{address}"){
    call.respondText("Address: ${call.parameters["address"]}")
  }
}