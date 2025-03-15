package com.example.studybuddybackend.routes

import com.example.studybuddybackend.repository.StudyBuddiesRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.delete

fun Route.studyBuddiesRoutes() {

    // Get all study buddies of a user
    get("/users/{userId}/buddies") {
        val userId = call.parameters["userId"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing user ID.")
        val repo = StudyBuddiesRepository()
        val buddies = repo.getBuddiesForUser(userId)
        call.respond(HttpStatusCode.OK, buddies)
    }

    // Create a new StudyBuddy friendship
    post("/users/{userId}/buddies/{buddyId}") {
        val userId = call.parameters["userId"]?.toLongOrNull()
            ?: return@post call.respond(HttpStatusCode.BadRequest, "Invalid or missing user ID.")
        val buddyId = call.parameters["buddyId"]?.toLongOrNull()
            ?: return@post call.respond(HttpStatusCode.BadRequest, "Invalid or missing buddy ID.")

        val repo = StudyBuddiesRepository()
        val pair = repo.addBuddy(userId, buddyId)
        if (pair != null) {
            call.respond(HttpStatusCode.Created, pair)
        } else {
            call.respond(HttpStatusCode.InternalServerError, "Failed to add buddy relationship.")
        }
    }

    // Delete a StudyBuddy friendship
    delete("/users/{userId}/buddies/{buddyId}") {
        val userId = call.parameters["userId"]?.toLongOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid or missing user ID.")
        val buddyId = call.parameters["buddyId"]?.toLongOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid or missing buddy ID.")

        val repo = StudyBuddiesRepository()
        val removed = repo.removeBuddy(userId, buddyId)
        if (removed) {
            call.respond(HttpStatusCode.OK, "Buddy relationship removed successfully.")
        } else {
            call.respond(HttpStatusCode.NotFound, "Buddy relationship not found.")
        }
    }
}
