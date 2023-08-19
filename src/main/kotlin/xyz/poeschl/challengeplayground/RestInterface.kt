package xyz.poeschl.challengeplayground

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class RestInterface(port: Int, private val playgroundPainter: PlaygroundDrawer) {

    private val server = embeddedServer(Netty, port = port) {
        routing {
            get("/ping") {
                println("Ping")
                call.respondText("Ping", ContentType.Text.Plain)
            }
            get("/blank/center") {
                println("Blanking center (${call.request.local.remoteHost})")
                playgroundPainter.blankCenter()
                call.respond(HttpStatusCode.OK, "Blanked center")
            }
            get("/blank/{id}") {
                val id = call.parameters["id"]?.toInt()
                if (id != null) {
                    println("Blanking boxid '$id' (${call.request.local.remoteHost})")
                    playgroundPainter.blankPlaybox(id)
                    call.respond(HttpStatusCode.OK, "Blanked boxid $id")
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid boxid")
                }
            }
            get("/blank/{id}/{sector}") {
                val id = call.parameters["id"]?.toInt()
                val sector = call.parameters["sector"]?.toInt()
                if (id != null && sector != null) {
                    println("Blanking sector '$sector' on boxid '$id' (${call.request.local.remoteHost})")
                    playgroundPainter.blankPlayboxSector(id, sector)
                    call.respond(HttpStatusCode.OK, "Blanked sector $sector in boxid $id")
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid boxid or sector")
                }
            }
        }
    }

    fun start() {
        server.start()
    }

    fun stop() {
        server.stop(500, 1000)
    }
}
