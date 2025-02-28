package com.example.studybuddybackend.database.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone

object Messages : Table("messages") {
    val id = long("message_id").autoIncrement()
    override val primaryKey = PrimaryKey(id)

    val chatRoomId = reference("chatroom_id", ChatRooms.id) //Messages linked to one chatroom
    val senderId = reference("sender_id", StudentUsers.id)
    val content = text("content")
    val messageImages = binary("message_images") //Could also use blob("message_images")
    val messageSentTimestamp = timestampWithTimeZone("message_sent_timestamp")
}
