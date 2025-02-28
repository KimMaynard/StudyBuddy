package com.example.studybuddybackend.database.entities

import org.jetbrains.exposed.sql.Table

object StudentUserClasses : Table("student_user_classes") {
    val userId = reference("user_id", StudentUsers.id)
    val classId = reference("class_id", Classes.id)
    override val primaryKey = PrimaryKey(userId, classId)
}
