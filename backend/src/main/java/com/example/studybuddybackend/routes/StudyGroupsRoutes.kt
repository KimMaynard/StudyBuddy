package com.example.studybuddybackend.routes

import com.example.studybuddybackend.repository.StudyGroupsRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*

// DTO for creating a study group
data class StudyGroupCreateDTO(
    val groupName: String,
    val description: String
)

// DTO for updating a study group
data class StudyGroupUpdateDTO(
    val groupName: String,
    val description: String
)

fun Route.studyGroupsRoutes() {
    val repository = StudyGroupsRepository()

    // Get all study groups
    get("/studygroups") {
        val groups = repository.getAllStudyGroups()
        call.respond(HttpStatusCode.OK, groups)
    }

    // Get a specific study group by id
    get("/studygroups/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing study group id.")
        val group = repository.getStudyGroupById(id)
        if (group == null) {
            call.respond(HttpStatusCode.NotFound, "Study group not found.")
        } else {
            call.respond(HttpStatusCode.OK, group)
        }
    }

    // Create a new study group
    post("/studygroups") {
        val dto = call.receive<StudyGroupCreateDTO>()
        val createdGroup = repository.createStudyGroup(dto.groupName, dto.description)
        call.respond(HttpStatusCode.Created, createdGroup)
    }

    // Update an existing study group
    put("/studygroups/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid or missing study group id.")
        val dto = call.receive<StudyGroupUpdateDTO>()
        val updated = repository.updateStudyGroup(id, dto.groupName, dto.description)
        if (updated) {
            call.respond(HttpStatusCode.OK, "Study group updated successfully.")
        } else {
            call.respond(HttpStatusCode.NotFound, "Study group not found.")
        }
    }

    // Delete a study group
    delete("/studygroups/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid or missing study group id.")
        val deleted = repository.deleteStudyGroup(id)
        if (deleted) {
            call.respond(HttpStatusCode.OK, "Study group deleted successfully.")
        } else {
            call.respond(HttpStatusCode.NotFound, "Study group not found.")
        }
    }
}
