package com.example.studybuddybackend.repository

import com.example.studybuddybackend.database.entities.Messages
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.OffsetDateTime

data class MessageEntity(
    val id: Long? = null,
    val chatRoomId: Long,
    val senderId: Long,
    val content: String,
    val messageImages: ByteArray?, // nullable
    val messageSentTimestamp: OffsetDateTime
)

private fun rowToMessageEntity(row: ResultRow): MessageEntity {
    return MessageEntity(
        id = row[Messages.id],
        chatRoomId = row[Messages.chatRoomId],
        senderId = row[Messages.senderId],
        content = row[Messages.content],
        messageImages = row[Messages.messageImages],
        messageSentTimestamp = row[Messages.messageSentTimestamp]
    )
}

class MessagesRepository {

    // Create a new message
    fun createMessage(messageEntity: MessageEntity): MessageEntity = transaction {
        val newId = Messages.insert {
            it[chatRoomId] = messageEntity.chatRoomId
            it[senderId] = messageEntity.senderId
            it[content] = messageEntity.content
            it[messageImages] = messageEntity.messageImages  // Now nullable insertion
            it[messageSentTimestamp] = messageEntity.messageSentTimestamp
        } get Messages.id
        messageEntity.copy(id = newId)
    }

    // Get all messages
    fun getAllMessages(): List<MessageEntity> = transaction {
        Messages.selectAll().map(::rowToMessageEntity)
    }

    // Get all messages for a specific chat room
    fun getMessagesByChatRoom(chatRoomId: Long): List<MessageEntity> = transaction {
        Messages.selectAll()
            .andWhere { Messages.chatRoomId eq chatRoomId }
            .map(::rowToMessageEntity)
    }

    // Gets a message by its iD
    fun getMessageById(id: Long): MessageEntity? = transaction {
        Messages.selectAll()
            .andWhere { Messages.id eq id }
            .map(::rowToMessageEntity)
            .singleOrNull()
    }

    // Gets a message if it exists in a specific chatroom
    fun getMessageInChatRoom(chatRoomId: Long, messageId: Long): MessageEntity? = transaction {
        Messages.selectAll()
            .andWhere { (Messages.chatRoomId eq chatRoomId) and (Messages.id eq messageId) }
            .map(::rowToMessageEntity)
            .singleOrNull()
    }

    // Update a message with id
    fun updateMessage(id: Long, updatedMessage: MessageEntity): Boolean = transaction {
        Messages.update({ Messages.id eq id }) {
            it[chatRoomId] = updatedMessage.chatRoomId
            it[senderId] = updatedMessage.senderId
            it[content] = updatedMessage.content
            it[messageImages] = updatedMessage.messageImages  // Allow updating to null
            // it[messageSentTimestamp] = updatedMessage.messageSentTimestamp - message original timestamp should not change after edits
        } > 0
    }

    // Delete a message with id
    fun deleteMessage(id: Long): Boolean = transaction {
        Messages.deleteWhere { Messages.id eq id } > 0
    }

}
