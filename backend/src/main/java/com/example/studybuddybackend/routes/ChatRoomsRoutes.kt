package com.example.studybuddybackend.routes

import com.example.studybuddybackend.repository.ChatRoomEntity
import com.example.studybuddybackend.repository.ChatRoomRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import java.time.OffsetDateTime

// DTO for chatroom data
data class ChatRoomDTO(
    val name: String,
    val studyType: String,
    val createdAt: String,
    val studyGroupId: Long
)

fun Route.chatRoomsRoutes() {

    route("/chatrooms") {

        // Get all chatrooms
        get {
            val repository = ChatRoomRepository()
            val chatRooms = repository.getAllChatRooms()
            call.respond(HttpStatusCode.OK, chatRooms)
        }

        // Get a chat room by id
        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "Invalid or missing chatroom id."
            )
            val repository = ChatRoomRepository()
            val chatRoom = repository.getChatRoomById(id)
            if (chatRoom == null) {
                call.respond(HttpStatusCode.NotFound, "Chatroom not found.")
            } else {
                call.respond(HttpStatusCode.OK, chatRoom)
            }
        }

        // Create a new chatroom
        post {
            val chatRoomDTO = call.receive<ChatRoomDTO>()
            val createdAt = OffsetDateTime.parse(chatRoomDTO.createdAt)
            // createdAt should be set automatically
            val newChatRoom = ChatRoomEntity(
                id = null,
                name = chatRoomDTO.name,
                studyType = chatRoomDTO.studyType,
                createdAt = createdAt,
                studyGroupId = chatRoomDTO.studyGroupId
            )
            val repository = ChatRoomRepository()
            val createdChatRoom = repository.createChatRoom(newChatRoom)
            call.respond(HttpStatusCode.Created, createdChatRoom)
        }

        // Update a chatroom
        put("/{id}") {

            val id = call.parameters["id"]?.toLongOrNull() ?: return@put call.respond(
                HttpStatusCode.BadRequest,
                "Invalid or missing chatroom id."
            )

            // createdAt in this DTO is ignored - createdAt should not be updatable
            val chatRoomDTO = call.receive<ChatRoomDTO>()

            // Get current chatroom to keep the original createdAt value
            val repository = ChatRoomRepository()
            val currentChatRoom = repository.getChatRoomById(id)
                ?: return@put call.respond(HttpStatusCode.NotFound, "Chatroom not found.")
            val updatedChatRoom = ChatRoomEntity(
                id = id,
                name = chatRoomDTO.name,
                studyType = chatRoomDTO.studyType,
                createdAt = currentChatRoom.createdAt, // keeps original createdAt value
                studyGroupId = chatRoomDTO.studyGroupId
            )

            val updated = repository.updateChatRoom(id, updatedChatRoom)
            if (updated) {
                call.respond(HttpStatusCode.OK, "Chatroom updated successfully.")
            } else {
                call.respond(HttpStatusCode.NotFound, "Chatroom not found.")
            }
        }

        // Delete a chatroom
        delete("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                "Invalid or missing chatroom id."
            )
            val repository = ChatRoomRepository()
            val deleted = repository.deleteChatRoom(id)
            if (deleted) {
                call.respond(HttpStatusCode.OK, "Chatroom deleted successfully.")
            } else {
                call.respond(HttpStatusCode.NotFound, "Chatroom not found.")
            }
        }
    }
}
