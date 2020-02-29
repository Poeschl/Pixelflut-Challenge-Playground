package io.github.poeschl.pixelflutchallenge.borders

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

class RestInterface(port: Int, private val playgroundPainter: PlaygroundDrawer) {

    private val server = embeddedServer(Netty, port = port) {
        routing {
            get("/ping") {
                println("Ping")
                call.respondText("Ping", ContentType.Text.Plain)
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
