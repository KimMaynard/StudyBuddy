package com.example.studybuddybackend.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import com.example.studybuddybackend.database.entities.StudentUsers
import com.example.studybuddybackend.database.entities.*

import org.slf4j.LoggerFactory

object DatabaseConnectionInit {

    private val logger = LoggerFactory.getLogger(DatabaseConnectionInit::class.java)

    fun init() {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/studybuddydatabase",
            driver = "org.postgresql.Driver",
            user = "studybuddy_user",
            password = "pass1234word5"
        )
        logger.info("StudyBuddyDatabase connected to successfully as studybuddy_user.")

        transaction {
            SchemaUtils.create( //Import tables if they do not already exist
                StudentUsers, Majors, Interests, Classes,
                StudentUserMajors, StudentUserInterests, StudentUserClasses,
                Universities, StudyGroups, StudyGroupMembers,
                ChatRooms, ChatRoomMembers, Messages, StudyBuddies
            )
        }
        logger.info("StudyBuddyDatabase schema created/verified successfully.")
    }
}
