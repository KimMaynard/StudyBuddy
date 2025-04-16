package com.example.studybuddybackend.routes

import com.example.studybuddybackend.repository.ClassEntity
import com.example.studybuddybackend.repository.ClassesRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.classesRoutes() {

    // Get all classes
    get("/classes") {
        val classes = ClassesRepository.getAllClasses()
        call.respond(HttpStatusCode.OK, classes)
    }

    // Get a specific class by its id
    get("/classes/{id}") {
        val id = call.parameters["id"]?.toLongOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing class id.")
        val classEntity = ClassesRepository.getClassById(id)
            ?: return@get call.respond(HttpStatusCode.NotFound, "Class not found.")
        call.respond(HttpStatusCode.OK, classEntity)
    }

    // Create a new class
    post("/classes") {
        val classDTO = call.receive<ClassEntity>()
        val createdClass = ClassesRepository.createClass(classDTO)
        call.respond(HttpStatusCode.Created, createdClass)
    }

    // Update an existing class
    put("/classes/{id}") {
        val id = call.parameters["id"]?.toLongOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid or missing class id.")
        val classDTO = call.receive<ClassEntity>()
        val updated = ClassesRepository.updateClass(id, classDTO)
        if (updated) {
            call.respond(HttpStatusCode.OK, "Class updated successfully.")
        } else {
            call.respond(HttpStatusCode.NotFound, "Class not found.")
        }
    }

    // Delete a class
    delete("/classes/{id}") {
        val id = call.parameters["id"]?.toLongOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid or missing class id.")
        val deleted = ClassesRepository.deleteClass(id)
        if (deleted) {
            call.respond(HttpStatusCode.OK, "Class deleted successfully.")
        } else {
            call.respond(HttpStatusCode.NotFound, "Class not found.")
        }
    }

}
