package com.example.studybuddybackend

//import io.ktor.server.plugins.calllogging.*

import io.ktor.http.ContentType
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun main(args: Array<String>) {
    //embeddedServer(Netty, port = 8081, module = Application::module).start(wait = true)
    embeddedServer(Netty, 8081) {
        routing {
            get("/") {
                call.respondText("Hello, world!", ContentType.Text.Html)
            }
        }
    }.start(wait = true)


}

fun Application.module() {
    install(ContentNegotiation) {
        jackson()
    }
    //install(CallLogging)

    routing {
        get("/") {
            call.respondText("Hello from StudyBuddy Backend!", ContentType.Text.Plain)
        }
    }
}
