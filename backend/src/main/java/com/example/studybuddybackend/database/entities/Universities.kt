package com.example.studybuddybackend.database.entities

import org.jetbrains.exposed.sql.Table

object Universities : Table("universities") {
    val id = long("university_id").autoIncrement()
    override val primaryKey = PrimaryKey(id)

    val name = varchar("name", 200)
    val address = text("address")
    val website = text("website")

    //Many-to-one relation with StudentUsers
    val userId = reference("user_id", StudentUsers.id)
}
