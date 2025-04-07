package com.example.studybuddybackend.routes

import com.example.studybuddybackend.repository.StudentUserInterestsRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.Route

fun Route.studentUserInterestsRoutes(){

    // get all interests associated with a user
    get("/students/{userId}/interests"){
        val userId = call.parameters["userId"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing user ID.")
        val repository = StudentUserInterestsRepository()
        val associations = repository.getAllInterestsByUserId(userId)
        call.respond(HttpStatusCode.OK, associations)
    }

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