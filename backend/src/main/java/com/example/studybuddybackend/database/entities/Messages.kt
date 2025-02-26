package com.example.studybuddybackend.database.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone

object Messages : Table("messages") {
    val id = long("message_id").autoIncrement()
    override val primaryKey = PrimaryKey(id)

    val chatRoomId = reference("chatroom_id", ChatRooms.id) // Linked to ChatRoom
    val senderId = reference("sender_id", StudentUsers.id)
    val content = text("content")
    val timestamp = timestampWithTimeZone("timestamp")
}
