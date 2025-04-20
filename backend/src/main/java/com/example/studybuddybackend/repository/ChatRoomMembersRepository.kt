package com.example.studybuddybackend.repository

import com.example.studybuddybackend.database.entities.ChatRoomMembers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.OffsetDateTime

data class ChatRoomMemberEntity(
    val chatRoomId: Long,
    val userId: Long,
    val studyGroupId: Long,
    val role: String,
    val joinedAt: OffsetDateTime
)

private fun rowToChatRoomMemberEntity(row: ResultRow): ChatRoomMemberEntity {
    return ChatRoomMemberEntity(
        chatRoomId = row[ChatRoomMembers.chatRoomId],
        userId = row[ChatRoomMembers.userId],
        studyGroupId = row[ChatRoomMembers.studyGroupId],
        role = row[ChatRoomMembers.role],
        joinedAt = row[ChatRoomMembers.joinedAt]
    )
}

class ChatRoomMembersRepository {


    // inserts chat room member into table
    fun createChatRoomMember(chatRoomMemberEntity: ChatRoomMemberEntity): ChatRoomMemberEntity = transaction {
        val inserted = ChatRoomMembers.insert {
            it[chatRoomId] = chatRoomMemberEntity.chatRoomId
            it[userId] = chatRoomMemberEntity.userId
            it[studyGroupId] = chatRoomMemberEntity.studyGroupId
            it[role] = chatRoomMemberEntity.role
            it[joinedAt] = chatRoomMemberEntity.joinedAt
        }
        chatRoomMemberEntity

    }

    //lists all chat room members with a specified chat room id
    fun getAllChatRoomMembers(chatRoomId: Long): List<ChatRoomMemberEntity> = transaction {
        val condition = SqlExpressionBuilder.run{
            ChatRoomMembers.chatRoomId eq chatRoomId
        }
        ChatRoomMembers.select(condition).map(:: rowToChatRoomMemberEntity)
    }

    // finds a member based on their ID and the ID of their chat room
    fun getByChatRoomMemberId(chatRoomId: Long, userId: Long): ChatRoomMemberEntity? = transaction {

        val condition = SqlExpressionBuilder.run{
            ChatRoomMembers.chatRoomId eq chatRoomId
            ChatRoomMembers.userId eq userId
        }

        ChatRoomMembers.select(condition)
            .map { row -> rowToChatRoomMemberEntity(row) }
            .singleOrNull()
    }

    //commented out because not sure if updating a member in this table is necessary with
    //deleting and adding functions available
    
    //fun updateChatRoomMembers(chatRoomId: Long, updatedChatRoomMember: ChatRoomMemberEntity): Boolean = transaction {
    //    ChatRoomMembers.update({ChatRoomMembers.chatRoomId eq chatRoomId}){
    //       it[userId] = ChatRoomMembers.userId
    //        it[studyGroupId] = ChatRoomMembers.studyGroupId
    //        it[role] = ChatRoomMembers.role
    //        it[joinedAt] = ChatRoomMembers.joinedAt
    //    } > 0
    //}
    //

    fun deleteChatRoomMember(chatRoomId: Long, userId: Long): Boolean = transaction {
        ChatRoomMembers.deleteWhere { (ChatRoomMembers.chatRoomId eq chatRoomId) and
                (ChatRoomMembers.userId eq userId) }
    } > 0
}