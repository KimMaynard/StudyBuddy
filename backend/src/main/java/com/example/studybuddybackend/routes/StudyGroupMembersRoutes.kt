package com.example.studybuddybackend.routes

import com.example.studybuddybackend.repository.ChatRoomMembersRepository
import com.example.studybuddybackend.repository.StudentUserInterestsRepository
import com.example.studybuddybackend.repository.StudyGroupMembersRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.Route

fun Route.studyGroupMembersRoutes(){
    get("/Groups/{groupId}/Students"){
        val groupId = call.parameters["groupId"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing group ID.")
        val repository = StudyGroupMembersRepository()
        val groupUsers = repository.getAllStudyGroupMembers(groupId)
        call.respond(HttpStatusCode.OK, groupUsers)
    }

    get("/students/{userId}/groups"){
        val userId = call.parameters["userId"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing user ID.")
        val repository = StudyGroupMembersRepository()
        val membersGroups = repository.getAllMembersStudyGroups(userId)
        call.respond(HttpStatusCode.OK, membersGroups)
    }

    post("/students/{userId}/groups/{groupId}"){
        val userId = call.parameters["userId"]?.toLongOrNull()
            ?: return@post call.respond(HttpStatusCode.BadRequest, "Invalid or missing user ID.")
        val groupId = call.parameters["groupId"]?.toLongOrNull()
            ?: return@post call.respond(HttpStatusCode.BadRequest, "Invalid or missing group ID.")
        val repository = StudyGroupMembersRepository()
        val association = repository.createStudyGroupMember(userId, groupId)

        if(association != null){
            call.respond(HttpStatusCode.Created, association)
        }else{
            call.respond(HttpStatusCode.InternalServerError, "Failed to add user to Group.")
        }
    }

    delete("/students/{userId}/groups/{groupId}"){
        val userId = call.parameters["userId"]?.toLongOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid or missing user ID.")
        val groupId = call.parameters["groupId"]?.toLongOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid or missing group ID.")

        val repository = StudyGroupMembersRepository()
        val removed = repository.deleteChatRoomMember(groupId, userId)

        if (removed){
            call.respond(HttpStatusCode.OK, "User removed from group successfully.")
        }else{
            call.respond(HttpStatusCode.NotFound, "User not found.")
        }
    }
}