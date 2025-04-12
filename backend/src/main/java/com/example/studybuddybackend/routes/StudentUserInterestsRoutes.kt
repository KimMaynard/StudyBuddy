package com.example.studybuddybackend.routes

import com.example.studybuddybackend.repository.StudentUserInterestsRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.Route

fun Route.studentUserInterestsRoutes(){

    // Gets all interests associated with a user
    get("/students/{userId}/interests"){
        val userId = call.parameters["userId"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing user ID.")
        val repository = StudentUserInterestsRepository()
        val interestsOfAUser = repository.getAllInterestsByUserId(userId)
        call.respond(HttpStatusCode.OK, interestsOfAUser)
    }

    // Gets all users of a specific interest
    get("/interests/{interestId}/users") {
        val interestId = call.parameters["interestId"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing interest ID.")
        val repository = StudentUserInterestsRepository()
        val usersWithInterest = repository.getAllStudentByInterestId(interestId)
        call.respond(HttpStatusCode.OK, usersWithInterest)
    }

    // Gets all interests of a student user
    post("/students/{userId}/interests/{interestId}"){
        val userId = call.parameters["userId"]?.toLongOrNull()
            ?: return@post call.respond(HttpStatusCode.BadRequest, "Invalid or missing user ID.")
        val interestId = call.parameters["interestId"]?.toLongOrNull()
            ?: return@post call.respond(HttpStatusCode.BadRequest, "Invalid or missing interest ID.")

        val repository = StudentUserInterestsRepository()
        val association = repository.createStudentUserInterest(userId, interestId)

        if (association != null){
            call.respond(HttpStatusCode.Created, association)
        }else{
            call.respond(HttpStatusCode.InternalServerError, "Failed to associate interest with user.")
        }
    }

    // Deletes a student user's interest
    delete("/students/{userId}/interests/{interestId}"){
        val userId = call.parameters["userId"]?.toLongOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid or missing user ID.")
        val interestId = call.parameters["interestId"]?.toLongOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid or missing interest ID.")

        val repository = StudentUserInterestsRepository()
        val removed = repository.deleteStudentUserInterest(userId, interestId)

        if (removed){
            call.respond(HttpStatusCode.OK, "Interest removed from user successfully.")
        }else{
            call.respond(HttpStatusCode.NotFound, "Interest not found.")
        }
    }
}