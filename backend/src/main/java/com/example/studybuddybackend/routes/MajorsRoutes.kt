package com.example.studybuddybackend.routes

import com.example.studybuddybackend.repository.MajorsRepository
import com.example.studybuddybackend.repository.MajorEntity
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.Route

// DTO to receive major data from the client
data class MajorDTO(val name: String)

fun Route.majorsRoutes() {

    // Get all majors
    get("/majors") {
        val repository = MajorsRepository()
        val majors = repository.getAllMajors()
        call.respond(HttpStatusCode.OK, majors)
    }

    // Get a specific major by id
    get("/majors/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing major id.")

        val repository = MajorsRepository()
        val major = repository.getMajorById(id)
        if (major == null) {
            call.respond(HttpStatusCode.NotFound, "Major not found.")
        } else {
            call.respond(HttpStatusCode.OK, major)
        }
    }

    // Create a new major
    post("/majors") {
        val majorDTO = call.receive<MajorDTO>()
        // Create a new MajorEntity. Assume id is null (auto-generated).
        val newMajor = MajorEntity(id = null, name = majorDTO.name)

        val repository = MajorsRepository()
        val createdMajor = repository.createMajor(newMajor)

        call.respond(HttpStatusCode.Created, createdMajor)
    }

    // Update an existing major
    put("/majors/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid or missing major id.")

        val majorDTO = call.receive<MajorDTO>()
        // Build a MajorEntity with the updated name
        val updatedMajor = MajorEntity(id = id, name = majorDTO.name)

        val repository = MajorsRepository()
        val updated = repository.updateMajor(id, updatedMajor)

        if (updated) {
            call.respond(HttpStatusCode.OK, "Major updated successfully.")
        } else {
            call.respond(HttpStatusCode.NotFound, "Major not found.")
        }
    }

    // Delete a major
    delete("/majors/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid or missing major id.")

        val repository = MajorsRepository()
        val deleted = repository.deleteMajor(id)

        if (deleted) {
            call.respond(HttpStatusCode.OK, "Major deleted successfully.")
        } else {
            call.respond(HttpStatusCode.NotFound, "Major not found.")
        }
    }
}
