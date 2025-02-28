package com.example.studybuddybackend.database.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone

object StudyGroupMembers : Table("study_group_members") {
    val groupId = reference("study_group_id", StudyGroups.id)
    val userId = reference("user_id", StudentUsers.id)
    val joinedAt = timestampWithTimeZone("joined_at")

    override val primaryKey = PrimaryKey(groupId, userId)
}
