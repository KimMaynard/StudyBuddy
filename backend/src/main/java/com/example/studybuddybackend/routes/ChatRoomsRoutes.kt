package com.example.studybuddybackend.routes

import com.example.studybuddybackend.repository.ChatRoomRepository
import com.example.studybuddybackend.repository.ChatRoomEntity
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.Route
import java.time.OffsetDateTime

// DTO for chat room data
data class chatRoomDTO(
    val name: String,
    val studyType: String,
    val qrCodeUrl: String,
    val qrCodeData: ByteArray,
    val createdAt: OffsetDateTime,
    val studyGroupId: Long
)

fun Route.chatRoomsRoutes(){

    // Get all chat rooms
    get("/chatrooms"){
        val repository = ChatRoomRepository()
        val chatRooms = repository.getAllChatRooms()
        call.respond(HttpStatusCode.OK, chatRooms)
    }

    //Get a chat room by id
    get("/chatrooms/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing Chatroom id")

        val repository = ChatRoomRepository()
        val chatRoom = repository.getChatRoomById(id)
        if (chatRoom == null){
            call.respond(HttpStatusCode.NotFound, "Chatroom not found.")
        } else{
            call.respond(HttpStatusCode.OK, chatRoom)
        }
    }

    // Create a new Chatroom
    post("/chatrooms"){
        val chatRoomDTO = call.receive<chatRoomDTO>()
        val newChatroom = ChatRoomEntity(
            id = null,
            name = chatRoomDTO.name,
            studyType = chatRoomDTO.studyType,
            qrCodeUrl = chatRoomDTO.qrCodeUrl,
            qrCodeData = chatRoomDTO.qrCodeData,
            createdAt = chatRoomDTO.createdAt,
            studyGroupId = chatRoomDTO.studyGroupId
            )
        val repository = ChatRoomRepository()
        val createdChatRoom = repository.createChatRoom(newChatroom)

        call.respond(HttpStatusCode.Created, createdChatRoom)
    }

    put("/chatroom/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid or missing chatroom id.")

        val chatRoomDTO = call.receive<chatRoomDTO>()
        val updatedChatRoom = ChatRoomEntity(id = id,
            name = chatRoomDTO.name,
            studyType = chatRoomDTO.studyType,
            qrCodeUrl = chatRoomDTO.qrCodeUrl,
            qrCodeData = chatRoomDTO.qrCodeData,
            createdAt =  chatRoomDTO.createdAt,
            studyGroupId = chatRoomDTO.studyGroupId)
        val repository = ChatRoomRepository()
        val updated = repository.updateChatRoom(id, updatedChatRoom)

        if(updated){
            call.respond(HttpStatusCode.OK, "Chatroom updated successfully.")
        }else{
            call.respond(HttpStatusCode.NotFound, "Chatroom not found")
        }
    }

    delete("/chatroom/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid or missing chatroom id.")

        val repository = ChatRoomRepository()
        val deleted = repository.deleteChatRoom(id)

        if (deleted) {
            call.respond(HttpStatusCode.OK, "Chatroom deleted successfully.")
        }else {
            call.respond(HttpStatusCode.NotFound, "Chatroom not found.")
        }

    }
}