package com.example.studybuddybackend.routes

import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.response.respondText

// Route for front-end providing HTTP endpoints for StudentUsers.kt
// Only a placeholder for now
fun Route.userRoutes() {
    get("/users") {
        call.respondText("Hello from /users route!")
    }
}
