package com.example.studybuddybackend.database.entities

import org.jetbrains.exposed.sql.Table

object Classes : Table("classes") {
    val id = long("id").autoIncrement()
    override val primaryKey = PrimaryKey(id)

    val className = varchar("class_name", 100)
    val classCode = varchar("class_code", 20)
}
