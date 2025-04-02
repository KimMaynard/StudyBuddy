package com.example.studybuddybackend.routes

import com.example.studybuddybackend.repository.StudyGroupEntity
import com.example.studybuddybackend.repository.StudyGroupsRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*

import java.time.OffsetDateTime

data class StudyGroupDTO(
    val groupName: String,
    val description: String,
    val dateCreated: String,
    val qrCodeUrl: String?,
    val qrCodeData: ByteArray?
)

fun Route.studyGroupsRoutes() {

    // Get all study groups
    get("/studygroups") {
        val repository = StudyGroupsRepository()
        val studyGroups = repository.getAllStudyGroups()
        call.respond(HttpStatusCode.OK, studyGroups)
    }

    // Get a specific study group by ID
    get("/studygroups/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing study group id.")
        val repository = StudyGroupsRepository()
        val studyGroup = repository.getStudyGroupById(id)
        if (studyGroup == null) {
            call.respond(HttpStatusCode.NotFound, "Study group not found.")
        } else {
            call.respond(HttpStatusCode.OK, studyGroup)
        }
    }

    // Create a new study group.
    post("/studygroups") {
        val studyGroupDTO = call.receive<StudyGroupDTO>()
        // Convert the ISO-8601 date string to OffsetDateTime
        val createdDate = OffsetDateTime.parse(studyGroupDTO.dateCreated)
        val newStudyGroup = StudyGroupEntity(
            id = null, // id is auto-generated
            groupName = studyGroupDTO.groupName,
            description = studyGroupDTO.description,
            dateCreated = createdDate,
            qrCodeUrl = studyGroupDTO.qrCodeUrl,
            qrCodeData = studyGroupDTO.qrCodeData
        )
        val repository = StudyGroupsRepository()
        val createdGroup = repository.createStudyGroup(newStudyGroup)
        call.respond(HttpStatusCode.Created, createdGroup)
    }

    // Update an existing study group
    put("/studygroups/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid or missing study group id.")
        val studyGroupDTO = call.receive<StudyGroupDTO>()
        val updatedDate = OffsetDateTime.parse(studyGroupDTO.dateCreated)
        val updatedStudyGroup = StudyGroupEntity(
            id = id,
            groupName = studyGroupDTO.groupName,
            description = studyGroupDTO.description,
            dateCreated = updatedDate,
            qrCodeUrl = studyGroupDTO.qrCodeUrl,
            qrCodeData = studyGroupDTO.qrCodeData
        )
        val repository = StudyGroupsRepository()
        val updated = repository.updateStudyGroup(id, updatedStudyGroup)
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
        val repository = StudyGroupsRepository()
        val deleted = repository.deleteStudyGroup(id)
        if (deleted) {
            call.respond(HttpStatusCode.OK, "Study group deleted successfully.")
        } else {
            call.respond(HttpStatusCode.NotFound, "Study group not found.")
        }
    }
}
