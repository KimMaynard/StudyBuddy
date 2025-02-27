package com.example.studybuddybackend.database.entities

import org.jetbrains.exposed.sql.Table

object StudentUserMajors : Table("student_user_majors") {
    val userId = reference("user_id", StudentUsers.id)
    val majorId = reference("major_id", Majors.id)
    override val primaryKey = PrimaryKey(userId, majorId)
}
