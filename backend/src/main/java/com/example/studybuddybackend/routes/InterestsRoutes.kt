package com.example.studybuddybackend.routes

import com.example.studybuddybackend.repository.InterestEntity
import com.example.studybuddybackend.repository.InterestsRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.Route

data class InterestDTO(val name: String)

fun Route.interestsRoutes() {

    // Gets all interests
    get("/interests") {
        val repository = InterestsRepository()
        val interests = repository.getAllInterests()
        call.respond(HttpStatusCode.OK, interests)
    }

    // Get a specific interest by their id
    get("/interests/{id}") {

        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing interest id.")

        val repository = InterestsRepository()
        val interest = repository.getInterestById(id)

        if (interest == null) {
            call.respond(HttpStatusCode.NotFound, "Interest not found.")
        } else {
            call.respond(HttpStatusCode.OK, interest)
        }
    }

    // Create a new interest
    post("/interests") {
        val interestDTO = call.receive<InterestDTO>()
        val newInterest = InterestEntity(id = null, name = interestDTO.name)
        val repository = InterestsRepository()
        val createdInterest = repository.createInterest(newInterest)
        call.respond(HttpStatusCode.Created, createdInterest)
    }

    // Update an existing interest
    put("/interests/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid or missing interest id.")
        val interestDTO = call.receive<InterestDTO>()
        val updatedInterest = InterestEntity(id = id, name = interestDTO.name)
        val repository = InterestsRepository()
        val updated = repository.updateInterest(id, updatedInterest)
        if (updated) {
            call.respond(HttpStatusCode.OK, "Interest updated successfully.")
        } else {
            call.respond(HttpStatusCode.NotFound, "Interest not found.")
        }
    }

    // Delete an interest
    delete("/interests/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid or missing interest id.")
        val repository = InterestsRepository()
        val deleted = repository.deleteInterest(id)
        if (deleted) {
            call.respond(HttpStatusCode.OK, "Interest deleted successfully.")
        } else {
            call.respond(HttpStatusCode.NotFound, "Interest not found.")
        }
    }
}
