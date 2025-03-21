package com.example.studybuddybackend.routes

import com.example.studybuddybackend.repository.StudentUserMajorsRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.Route

fun Route.studentUserMajorsRoutes() {

    // Get all major associations for a given student
    get("/students/{userId}/majors") {
        val userId = call.parameters["userId"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing user ID.")
        val repository = StudentUserMajorsRepository()
        val associations = repository.getAssociationsForUser(userId)
        call.respond(HttpStatusCode.OK, associations)
    }

    // Associate a major with a student
    post("/students/{userId}/majors/{majorId}") {
        val userId = call.parameters["userId"]?.toLongOrNull()
            ?: return@post call.respond(HttpStatusCode.BadRequest, "Invalid or missing user ID.")
        val majorId = call.parameters["majorId"]?.toLongOrNull()
            ?: return@post call.respond(HttpStatusCode.BadRequest, "Invalid or missing major ID.")

        val repository = StudentUserMajorsRepository()
        val association = repository.addMajorToStudent(userId, majorId)

        if (association != null) {
            call.respond(HttpStatusCode.Created, association)
        } else {
            call.respond(HttpStatusCode.InternalServerError, "Failed to associate major with user.")
        }
    }

    // Delete a major association from a student.
    delete("/students/{userId}/majors/{majorId}") {
        val userId = call.parameters["userId"]?.toLongOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid or missing user ID.")
        val majorId = call.parameters["majorId"]?.toLongOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid or missing major ID.")

        val repository = StudentUserMajorsRepository()
        val removed = repository.removeMajorFromStudent(userId, majorId)

        if (removed) {
            call.respond(HttpStatusCode.OK, "Major removed from user successfully.")
        } else {
            call.respond(HttpStatusCode.NotFound, "Association not found.")
        }
    }
}
