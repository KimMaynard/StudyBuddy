package com.example.studybuddybackend.database.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone

object ChatRooms : Table("chat_rooms") {
    val id = long("chatroom_id").autoIncrement()
    override val primaryKey = PrimaryKey(id)

    // Each chatroom belongs to a StudyGroup
    val groupId = reference("group_id", StudyGroups.id)
    val createdAt = timestampWithTimeZone("created_at")
}
