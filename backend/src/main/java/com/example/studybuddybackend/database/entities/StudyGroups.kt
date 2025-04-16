package com.example.studybuddybackend.database.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone

object StudyGroups : Table("study_groups") {
    val id = long("study_group_id").autoIncrement()
    override val primaryKey = PrimaryKey(id)

    val groupName = varchar("group_name", 200)
    val description = text("description")
    val dateCreated = timestampWithTimeZone("date_created")

    // No longer nullable - generated automatically at creation of a new studygroup
    val qrCodeUrl = text("qr_code_url")
    val qrCodeData = binary("qr_code_data")
}
