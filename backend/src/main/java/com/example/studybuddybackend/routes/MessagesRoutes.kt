package com.example.studybuddybackend.routes

import com.example.studybuddybackend.repository.MessageEntity
import com.example.studybuddybackend.repository.MessagesRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.Route
import java.time.OffsetDateTime
import java.util.Base64

data class MessageDTO(
    val chatRoomId: Long,
    val senderId: Long,
    val content: String,
    val messageImages: String?, // null if no image attached to message
    val messageSentTimestamp: String
)

fun Route.messagesRoutes() {

    // Gets all messages
    get("/messages") {
        val repository = MessagesRepository()
        val messages = repository.getAllMessages()
        call.respond(HttpStatusCode.OK, messages)
    }

    // Get a message by its id
    get("/messages/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing message id")
        val repository = MessagesRepository()
        val message = repository.getMessageById(id)
        if (message == null) {
            call.respond(HttpStatusCode.NotFound, "Message not found")
        } else {
            call.respond(HttpStatusCode.OK, message)
        }
    }

    // Gets all messages for a specific chatroom
    get("/chatrooms/{chatRoomId}/messages") {
        val chatRoomId = call.parameters["chatRoomId"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing chat room id")
        val repository = MessagesRepository()
        val messages = repository.getMessagesByChatRoom(chatRoomId)
        call.respond(HttpStatusCode.OK, messages)
    }

    // Gets a message by its id if it exists within a specified chatroom
    get("/chatrooms/{chatRoomId}/messages/{messageId}") {
        val chatRoomId = call.parameters["chatRoomId"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing chat room id")
        val messageId = call.parameters["messageId"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing message id")
        val repository = MessagesRepository()
        val message = repository.getMessageInChatRoom(chatRoomId, messageId)
        if (message == null) {
            call.respond(HttpStatusCode.NotFound, "Message not found in the specified chatroom")
        } else {
            call.respond(HttpStatusCode.OK, message)
        }
    }

    // Create a message
    post("/messages") {
        val dto = call.receive<MessageDTO>()
        val messageSentTimestamp = OffsetDateTime.parse(dto.messageSentTimestamp)
        // Decodes the Base64 messageImages if provided; otherwise remain null
        val messageImages: ByteArray? = dto.messageImages?.let { Base64.getDecoder().decode(it) }
        val newMessage = MessageEntity(
            id = null,
            chatRoomId = dto.chatRoomId,
            senderId = dto.senderId,
            content = dto.content,
            messageImages = messageImages,
            messageSentTimestamp = messageSentTimestamp
        )
        val repository = MessagesRepository()
        val createdMessage = repository.createMessage(newMessage)
        call.respond(HttpStatusCode.Created, createdMessage)
    }

    // Update a message
    put("/messages/{id}") {

        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid or missing message id")

        // DTO for current message
        val messageDTO = call.receive<MessageDTO>()

        // Gets current message entry
        val repo = MessagesRepository()
        val currentMessagesRepository = repo.getMessageById(id)
            ?: return@put call.respond(HttpStatusCode.NotFound, "Chatroom not found.")

        // CHECK LATER - does the original message attached image remain after an update?
        val messageImages: ByteArray? = messageDTO.messageImages?.let { Base64.getDecoder().decode(it) }

        val updatedMessage = MessageEntity(
            id = id,
            chatRoomId = messageDTO.chatRoomId,
            senderId = messageDTO.senderId,
            content = messageDTO.content,
            messageImages = messageImages,
            messageSentTimestamp = currentMessagesRepository.messageSentTimestamp // Keeps original timestamp
        )

        val repository = MessagesRepository()
        val updated = repository.updateMessage(id, updatedMessage)
        if (updated) {
            call.respond(HttpStatusCode.OK, "Message updated successfully")
        } else {
            call.respond(HttpStatusCode.NotFound, "Message not found")
        }
    }

    // Delete a message
    delete("/messages/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid or missing message id")
        val repository = MessagesRepository()
        val deleted = repository.deleteMessage(id)
        if (deleted) {
            call.respond(HttpStatusCode.OK, "Message deleted successfully")
        } else {
            call.respond(HttpStatusCode.NotFound, "Message not found")
        }
    }

}
