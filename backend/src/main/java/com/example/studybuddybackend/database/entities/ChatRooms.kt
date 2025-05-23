package com.example.studybuddybackend.database.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone

object ChatRooms : Table("chat_rooms") {
    val id = long("chatroom_id").autoIncrement()
    override val primaryKey = PrimaryKey(id)

    val name = varchar("name", 200)
    val studyType = varchar("study_type", 100)
    val createdAt = timestampWithTimeZone("created_at")

    // Each chatroom belongs to one Study Group
    val studyGroupId = reference("study_group_id", StudyGroups.id)
}
