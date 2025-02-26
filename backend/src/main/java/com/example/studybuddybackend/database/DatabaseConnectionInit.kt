package com.example.studybuddybackend.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import com.example.studybuddybackend.database.entities.StudentUsers

object DatabaseConnectionInit {
    fun init() {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/studybuddydatabase",
            driver = "org.postgresql.Driver",
            user = "studybuddy_user",
            password = "pass1234word5"
        )

        transaction {
            SchemaUtils.create(StudentUsers) // Add more tables here as needed
        }
    }
}
