package com.example.studybuddybackend.database.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone
import com.example.studybuddybackend.database.extensions.CommaSepListToTextArray

object StudentUsers : Table("student_users") {
    val id = long("user_id").autoIncrement()
    override val primaryKey = PrimaryKey(id)

    val firstName = varchar("first_name", 100)
    val middleName = varchar("middle_name", 50).nullable()
    val lastName = varchar("last_name", 100)
    val profilePicture = text("profile_picture").nullable()
    val username = varchar("username", 50).uniqueIndex()
    val email = varchar("email", 100).uniqueIndex()
    val phoneNumber = varchar("phone_number", 20).nullable()
    val password = varchar("password", 256)
    val currentDegree = varchar("current_degree", 50)
    val seniority = varchar("seniority", 50)

    // Now stored as TEXT[] in Postgres, but handled as a comma-separated String in Kotlin
    val majors = registerColumn<String>("majors", CommaSepListToTextArray)
    val interests = registerColumn<String>("interests", CommaSepListToTextArray)
    val classes = registerColumn<String>("classes", CommaSepListToTextArray)

    val preferredStudyStyle = varchar("preferred_study_style", 100)
    val dateCreated = timestampWithTimeZone("date_created")
}
