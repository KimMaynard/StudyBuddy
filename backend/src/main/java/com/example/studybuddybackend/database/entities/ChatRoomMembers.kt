package com.example.studybuddybackend.database.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone

object ChatRoomMembers : Table("chat_room_members") {
    val chatRoomId = reference("chatroom_id", ChatRooms.id)
    val userId = reference("user_id", StudentUsers.id)
    val studyGroupId = reference("study_group_id", StudyGroups.id)

    val role = varchar("role", 20)
    val joinedAt = timestampWithTimeZone("joined_at")

    override val primaryKey = PrimaryKey(chatRoomId, userId)
}
