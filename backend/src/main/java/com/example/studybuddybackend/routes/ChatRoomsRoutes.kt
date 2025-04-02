package com.example.studybuddybackend.routes

import com.example.studybuddybackend.repository.ChatRoomEntity
import com.example.studybuddybackend.repository.ChatRoomRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.delete
import io.ktor.server.routing.Route
import java.time.OffsetDateTime

data class ChatRoomDTO(
    val name: String,
    val studyType: String,
    val qrCodeUrl: String?,
    val qrCodeData: ByteArray?,
    val createdAt: String,
    val studyGroupId: Long
)

fun Route.chatRoomsRoutes() {

    // Get all chat rooms
    get("/chatrooms") {
        val repository = ChatRoomRepository()
        val chatRooms = repository.getAllChatRooms()
        call.respond(HttpStatusCode.OK, chatRooms)
    }

    // Get a specific chat room by its id
    get("/chatrooms/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing chat room id.")
        val repository = ChatRoomRepository()
        val chatRoom = repository.getChatRoomById(id)
        if (chatRoom == null) {
            call.respond(HttpStatusCode.NotFound, "Chatroom not found.")
        } else {
            call.respond(HttpStatusCode.OK, chatRoom)
        }
    }

    // Create a new chat room
    post("/chatrooms") {
        val chatRoomDTO = call.receive<ChatRoomDTO>()
        // Convert the ISO-8601 string into an OffsetDateTime
        val createdAt = OffsetDateTime.parse(chatRoomDTO.createdAt)
        val newChatRoom = ChatRoomEntity(
            id = null,
            name = chatRoomDTO.name,
            studyType = chatRoomDTO.studyType,
            qrCodeUrl = chatRoomDTO.qrCodeUrl,
            qrCodeData = chatRoomDTO.qrCodeData,
            createdAt = createdAt,
            studyGroupId = chatRoomDTO.studyGroupId
        )
        val repository = ChatRoomRepository()
        val createdChatRoom = repository.createChatRoom(newChatRoom)
        call.respond(HttpStatusCode.Created, createdChatRoom)
    }

    // Update an existing chat room
    put("/chatrooms/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid or missing chat room id.")
        val chatRoomDTO = call.receive<ChatRoomDTO>()
        val updatedCreatedAt = OffsetDateTime.parse(chatRoomDTO.createdAt)
        val updatedChatRoom = ChatRoomEntity(
            id = id,
            name = chatRoomDTO.name,
            studyType = chatRoomDTO.studyType,
            qrCodeUrl = chatRoomDTO.qrCodeUrl,
            qrCodeData = chatRoomDTO.qrCodeData,
            createdAt = updatedCreatedAt,
            studyGroupId = chatRoomDTO.studyGroupId
        )
        val repository = ChatRoomRepository()
        val updated = repository.updateChatRoom(id, updatedChatRoom)
        if (updated) {
            call.respond(HttpStatusCode.OK, "Chatroom updated successfully.")
        } else {
            call.respond(HttpStatusCode.NotFound, "Chatroom not found.")
        }
    }

    // Delete a chat room
    delete("/chatrooms/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid or missing chat room id.")
        val repository = ChatRoomRepository()
        val deleted = repository.deleteChatRoom(id)
        if (deleted) {
            call.respond(HttpStatusCode.OK, "Chatroom deleted successfully.")
        } else {
            call.respond(HttpStatusCode.NotFound, "Chatroom not found.")
        }
    }
}
