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
    val qrCodeUrl: String?,
    val qrCodeData: ByteArray?,
    val createdAt: OffsetDateTime,
    val studyGroupId: Long
)

private fun rowToChatRoomEntity(row: ResultRow): ChatRoomEntity {
    return ChatRoomEntity(
        id = row[ChatRooms.id],
        name = row[ChatRooms.name],
        studyType = row[ChatRooms.studyType],
        qrCodeUrl = row[ChatRooms.qrCodeUrl],
        qrCodeData = row[ChatRooms.qrCodeData],
        createdAt = row[ChatRooms.createdAt],
        studyGroupId = row[ChatRooms.studyGroupId]
    )
}
class ChatRoomRepository {

    //Create
    fun createChatRoom(chatRoomEntity: ChatRoomEntity): ChatRoomEntity = transaction {
        val newId = ChatRooms.insert {
            it[name] = chatRoomEntity.name
            it[studyType] = chatRoomEntity.studyType
            it[qrCodeUrl] = chatRoomEntity.qrCodeUrl
            it[qrCodeData] = chatRoomEntity.qrCodeData
            it[createdAt] = chatRoomEntity.createdAt
            it[studyGroupId] = chatRoomEntity.studyGroupId
        } get ChatRooms.id // Returns the generated ID
        chatRoomEntity.copy(id = newId) // classEntity.id would be null, so we need to copy the new ID instead
    }

    // Read all
   fun getAllChatRooms(): List<ChatRoomEntity> = transaction {
       ChatRooms.selectAll().map(::rowToChatRoomEntity)
   }

    // Get a chatroom given its id
    fun getChatRoomById(id: Long): ChatRoomEntity? = transaction {

        val condition = SqlExpressionBuilder.run {
            ChatRooms.id eq id
        }

        ChatRooms.select(condition)
            .map { row -> rowToChatRoomEntity(row) } // Now able to map the row to a room entity
            .singleOrNull()
    }

    //Update
    fun updateChatRoom(id: Long, updatedChatRoom: ChatRoomEntity): Boolean = transaction {
        ChatRooms.update({ChatRooms.id eq id  }){
            it[name] = updatedChatRoom.name
            it[studyType] = updatedChatRoom.studyType
            it[qrCodeUrl] = updatedChatRoom.qrCodeUrl
            it[qrCodeData] = updatedChatRoom.qrCodeData
            it[createdAt] = updatedChatRoom.createdAt
            it[studyGroupId] = updatedChatRoom.studyGroupId
        } > 0
    }

    //Delete
    fun deleteChatRoom(id: Long): Boolean = transaction {
        ChatRooms.deleteWhere { ChatRooms.id eq id } > 0
    }






}