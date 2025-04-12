package com.example.studybuddybackend.routes

import com.example.studybuddybackend.repository.StudentUserClassesRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.studentUserClassesRoutes() {

    // Gets all classes associated with a specified user ID
    get("/students/{userId}/classes") {
        val userId = call.parameters["userId"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing user ID.")
        val repository = StudentUserClassesRepository()
        val associations = repository.getAllClassesByUserId(userId)
        call.respond(HttpStatusCode.OK, associations)
    }

    // Gets all students associated with a specific class ID
    get("/classes/{classId}/students") {
        val classId = call.parameters["classId"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing class ID.")

        val repository = StudentUserClassesRepository()
        val associations = repository.getAllStudentByClassId(classId)

        // Return the list of student-user–class associations for that class
        call.respond(HttpStatusCode.OK, associations)
    }

    // Creates a new association between a student user and a class
    post("/students/{userId}/classes/{classId}") {
        val userId = call.parameters["userId"]?.toLongOrNull()
            ?: return@post call.respond(HttpStatusCode.BadRequest, "Invalid or missing user ID.")
        val classId = call.parameters["classId"]?.toLongOrNull()
            ?: return@post call.respond(HttpStatusCode.BadRequest, "Invalid or missing class ID.")
        val repository = StudentUserClassesRepository()
        val association = repository.createStudentUserClasses(userId, classId)
        if (association != null) {
            call.respond(HttpStatusCode.Created, association)
        } else {
            call.respond(HttpStatusCode.InternalServerError, "Failed to associate class with user.")
        }
    }

    // Deletes an existing association between a student user and a class
    delete("/students/{userId}/classes/{classId}") {
        val userId = call.parameters["userId"]?.toLongOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid or missing user ID.")
        val classId = call.parameters["classId"]?.toLongOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid or missing class ID.")
        val repository = StudentUserClassesRepository()
        val removed = repository.deleteStudentUserClass(userId, classId)
        if (removed) {
            call.respond(HttpStatusCode.OK, "Class removed from user successfully.")
        } else {
            call.respond(HttpStatusCode.NotFound, "Class not found associated to such user.")
        }
    }
}
