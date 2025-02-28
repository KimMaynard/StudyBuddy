package com.example.studybuddybackend.database.entities

import org.jetbrains.exposed.sql.Table

object StudentUserInterests : Table("student_user_interests") {
    val userId = reference("user_id", StudentUsers.id)
    val interestId = reference("interest_id", Interests.id)
    override val primaryKey = PrimaryKey(userId, interestId)
}
