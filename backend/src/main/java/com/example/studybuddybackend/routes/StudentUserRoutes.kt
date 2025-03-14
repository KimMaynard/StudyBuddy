package com.example.studybuddybackend.routes

import com.example.studybuddybackend.repository.StudentUserEntity
import com.example.studybuddybackend.repository.StudentUsersRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import java.time.OffsetDateTime

// DTO for API input
data class UserDTO(
    val firstName: String,
    val middleName: String?,
    val lastName: String,
    val profilePicture: ByteArray?,  // Binary data for profile picture
    val username: String,
    val email: String,
    val areaCode: String?,
    val phoneNumber: String?,
    val password: String,
    val currentDegree: String,
    val seniority: String,
    val preferredStudyStyle: String
)

fun Route.studentUserRoutes() {

    // Get user by its id
    get("/users/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing user ID.")
        val user = StudentUsersRepository().getStudentUserById(id)
        if (user == null) {
            call.respond(HttpStatusCode.NotFound, "User with id $id not found.")
        } else {
            call.respond(HttpStatusCode.OK, user)
        }
    }

    // Get all users
    get("/users") {
        val users = StudentUsersRepository().getAllStudentUsers()
        call.respond(users)
    }

    // Create a new student user.
    post("/users") {
        val userDTO = call.receive<UserDTO>()
        val newUser = StudentUserEntity(
            id = null,
            firstName = userDTO.firstName,
            middleName = userDTO.middleName,
            lastName = userDTO.lastName,
            profilePicture = userDTO.profilePicture,
            username = userDTO.username,
            email = userDTO.email,
            areaCode = userDTO.areaCode,
            phoneNumber = userDTO.phoneNumber,
            password = userDTO.password,
            currentDegree = userDTO.currentDegree,
            seniority = userDTO.seniority,
            preferredStudyStyle = userDTO.preferredStudyStyle,
            dateCreated = OffsetDateTime.now() // Sets user date and time created to current
        )
        val createdUser = StudentUsersRepository().createStudentUser(newUser)
        call.respond(HttpStatusCode.Created, createdUser)
    }

    // Update an existing student user
    put("/users/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid or missing user ID.")
        val userDTO = call.receive<UserDTO>()
        val updatedUser = StudentUserEntity(
            id = id,
            firstName = userDTO.firstName,
            middleName = userDTO.middleName,
            lastName = userDTO.lastName,
            profilePicture = userDTO.profilePicture,
            username = userDTO.username,
            email = userDTO.email,
            areaCode = userDTO.areaCode,
            phoneNumber = userDTO.phoneNumber,
            password = userDTO.password,
            currentDegree = userDTO.currentDegree,
            seniority = userDTO.seniority,
            preferredStudyStyle = userDTO.preferredStudyStyle,
            dateCreated = OffsetDateTime.now() // Adjust as needed.
        )
        val updated = StudentUsersRepository().updateStudentUser(id, updatedUser)
        if (updated) {
            call.respond(HttpStatusCode.OK, "User updated successfully.")
        } else {
            call.respond(HttpStatusCode.NotFound, "User not found.")
        }
    }

    // Delete a student user
    delete("/users/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid or missing user ID.")
        val deleted = StudentUsersRepository().deleteStudentUser(id)
        if (deleted) {
            call.respond(HttpStatusCode.OK, "User deleted successfully.")
        } else {
            call.respond(HttpStatusCode.NotFound, "User not found.")
        }
    }
}
