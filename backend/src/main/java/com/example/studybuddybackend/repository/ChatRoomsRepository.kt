package com.example.studybuddybackend.repository

import com.example.studybuddybackend.database.entities.ChatRooms
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.OffsetDateTime

data class ChatRoomEntity(
    val id: Long? = null,
    val name: String,
    val studyType: String,
    val createdAt: OffsetDateTime,
    val studyGroupId: Long
)

private fun rowToChatRoomEntity(row: ResultRow): ChatRoomEntity {
    return ChatRoomEntity(
        id = row[ChatRooms.id],
        name = row[ChatRooms.name],
        studyType = row[ChatRooms.studyType],
        createdAt = row[ChatRooms.createdAt],
        studyGroupId = row[ChatRooms.studyGroupId]
    )
}

class ChatRoomRepository {

    // Create a new chatroom
    fun createChatRoom(chatRoomEntity: ChatRoomEntity): ChatRoomEntity = transaction {
        val newId = ChatRooms.insert {
            it[name] = chatRoomEntity.name
            it[studyType] = chatRoomEntity.studyType
            it[createdAt] = chatRoomEntity.createdAt
            it[studyGroupId] = chatRoomEntity.studyGroupId
        } get ChatRooms.id // Returns the generated ID
        chatRoomEntity.copy(id = newId) // classEntity.id would be null, so we need to copy the new ID instead
    }

    // Read all chat rooms
    fun getAllChatRooms(): List<ChatRoomEntity> = transaction {
        ChatRooms.selectAll().map(::rowToChatRoomEntity)
    }

    // Get a chatroom given its id
    fun getChatRoomById(id: Long): ChatRoomEntity? = transaction {
        ChatRooms.selectAll()
            .andWhere { ChatRooms.id eq id }
            .map(::rowToChatRoomEntity)
            .singleOrNull()
    }

    // Update a chatroom
    fun updateChatRoom(id: Long, updatedChatRoom: ChatRoomEntity): Boolean = transaction {
        ChatRooms.update({ ChatRooms.id eq id }) {
            it[name] = updatedChatRoom.name
            it[studyType] = updatedChatRoom.studyType
            // createdAt cannot be changed
            it[studyGroupId] = updatedChatRoom.studyGroupId
        } > 0
    }

    // Delete a chatroom
    fun deleteChatRoom(id: Long): Boolean = transaction {
        ChatRooms.deleteWhere { ChatRooms.id eq id } > 0
    }
}
