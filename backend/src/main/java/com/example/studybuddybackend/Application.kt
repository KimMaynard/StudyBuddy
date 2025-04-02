package com.example.studybuddybackend

import com.example.studybuddybackend.database.DatabaseConnectionInit
import com.example.studybuddybackend.routes.chatRoomsRoutes
import com.example.studybuddybackend.routes.interestsRoutes
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

// Route imports
import com.example.studybuddybackend.routes.studentUserRoutes
import com.example.studybuddybackend.routes.majorsRoutes
import com.example.studybuddybackend.routes.messagesRoutes
import com.example.studybuddybackend.routes.studentUserMajorsRoutes
import com.example.studybuddybackend.routes.universitiesRoutes
import com.example.studybuddybackend.routes.studyBuddiesRoutes
import com.example.studybuddybackend.routes.studyGroupsRoutes

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8081, module = Application::module).start(wait = true)
}

fun Application.module() {

    //Initialize StudyBuddyDatabase connection
    DatabaseConnectionInit.init()

    install(ContentNegotiation) {
        jackson{
            registerModule(com.fasterxml.jackson.datatype.jsr310.JavaTimeModule()) // For date attributes
        }
    }

    routing {
        get("/") {
            call.respondText("Hello from StudyBuddy Backend!", ContentType.Text.Plain)
        }
        studentUserRoutes()
        majorsRoutes()
        studentUserMajorsRoutes()
        universitiesRoutes()
        studyBuddiesRoutes()
        interestsRoutes()
        studyGroupsRoutes()
        chatRoomsRoutes()
        messagesRoutes()
    }

}
