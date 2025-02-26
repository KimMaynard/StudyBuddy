package com.example.studybuddybackend.database.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone

object ChatRoomMembers : Table("chat_room_members") {
    val chatRoomId = reference("chatroom_id", ChatRooms.id, onDelete = ReferenceOption.CASCADE)
    val userId = reference("user_id", StudentUsers.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(chatRoomId, userId)
}
