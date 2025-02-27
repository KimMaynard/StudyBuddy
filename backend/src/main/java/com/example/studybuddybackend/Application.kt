package com.example.studybuddybackend

import com.example.studybuddybackend.database.DatabaseConnectionInit
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

import com.example.studybuddybackend.routes.userRoutes

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8081, module = Application::module).start(wait = true)
}

fun Application.module() {

    //Initialize StudyBuddyDatabase connection
    DatabaseConnectionInit.init()

    install(ContentNegotiation) {
        jackson()
    }

    routing {
        get("/") {
            call.respondText("Hello from StudyBuddy Backend!", ContentType.Text.Plain)
        }
        userRoutes()
    }

}
