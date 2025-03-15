package com.example.studybuddybackend.database.entities

import org.jetbrains.exposed.sql.Table

object StudyBuddies : Table("study_buddies") {
    val userA = reference("user_a", StudentUsers.id)
    val userB = reference("user_b", StudentUsers.id)
    override val primaryKey = PrimaryKey(userA, userB)
}
