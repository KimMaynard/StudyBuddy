package com.example.studybuddybackend.database.entities

import org.jetbrains.exposed.sql.Table

object Interests : Table("interests") {
    val id = long("id").autoIncrement()
    override val primaryKey = PrimaryKey(id)

    val name = varchar("name", 100).uniqueIndex()
}
