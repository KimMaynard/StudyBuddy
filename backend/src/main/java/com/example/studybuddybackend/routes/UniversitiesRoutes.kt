package com.example.studybuddybackend.routes

import com.example.studybuddybackend.repository.UniversityEntity
import com.example.studybuddybackend.repository.UniversityRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.Route

// DTO for API input
data class UniversityDTO(
    val name: String,
    val address: String,
    val website: String,
    val userId: Long
)

fun Route.universitiesRoutes() {

    // Get all universities
    get("/universities") {
        val repository = UniversityRepository()
        val universities = repository.getAllUniversities()
        call.respond(HttpStatusCode.OK, universities)
    }

    // Get a university by its ID
    get("/universities/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing university id.")
        val repository = UniversityRepository()
        val university = repository.getUniversityById(id)
        if (university == null) {
            call.respond(HttpStatusCode.NotFound, "University not found.")
        } else {
            call.respond(HttpStatusCode.OK, university)
        }
    }

    // Create a new university
    post("/universities") {
        val universityDTO = call.receive<UniversityDTO>()
        val newUniversity = UniversityEntity(
            id = null, // Auto-generated by the database.
            name = universityDTO.name,
            address = universityDTO.address,
            website = universityDTO.website,
            userId = universityDTO.userId
        )
        val repository = UniversityRepository()
        val createdUniversity = repository.createUniversity(newUniversity)
        call.respond(HttpStatusCode.Created, createdUniversity)
    }

    // Update an university
    // Make sure to state the university ID when updating a university entry
    put("/universities/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid or missing university id.")
        val universityDTO = call.receive<UniversityDTO>()
        val updatedUniversity = UniversityEntity(
            id = id,
            name = universityDTO.name,
            address = universityDTO.address,
            website = universityDTO.website,
            userId = universityDTO.userId
        )
        val repository = UniversityRepository()
        val updated = repository.updateUniversity(id, updatedUniversity)
        if (updated) {
            call.respond(HttpStatusCode.OK, "University updated successfully.")
        } else {
            call.respond(HttpStatusCode.NotFound, "University not found.")
        }
    }

    // Delete a university.
    delete("/universities/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid or missing university id.")
        val repository = UniversityRepository()
        val deleted = repository.deleteUniversity(id)
        if (deleted) {
            call.respond(HttpStatusCode.OK, "University deleted successfully.")
        } else {
            call.respond(HttpStatusCode.NotFound, "University not found.")
        }
    }
}
