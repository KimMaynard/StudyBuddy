package com.example.studybuddybackend.repository

import com.example.studybuddybackend.database.entities.StudyGroups
import com.example.studybuddybackend.utils.QrCodeGenUtil
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.OffsetDateTime
import java.util.UUID

data class StudyGroupEntity(
    val id: Long? = null,
    val groupName: String,
    val description: String,
    val dateCreated: OffsetDateTime,
    val qrCodeUrl: String,
    val qrCodeData: ByteArray
)

private fun rowToStudyGroupEntity(row: ResultRow): StudyGroupEntity {
    return StudyGroupEntity(
        id = row[StudyGroups.id],
        groupName = row[StudyGroups.groupName],
        description = row[StudyGroups.description],
        dateCreated = row[StudyGroups.dateCreated],
        qrCodeUrl = row[StudyGroups.qrCodeUrl],
        qrCodeData = row[StudyGroups.qrCodeData]
    )
}


class StudyGroupsRepository {

    // Create a study group - dateCreated and QR code values automatically set
    fun createStudyGroup(groupName: String, description: String): StudyGroupEntity = transaction {

        val currentTime = OffsetDateTime.now()

        // Generates unique studygroup join URL
        val uniqueToken = UUID.randomUUID().toString()
        val joinUrl = "http://studybuddy.com/join/$uniqueToken"

        // Generate QR code using our utility
        val (generatedUrl, generatedQrData) = QrCodeGenUtil.generateQRCode(joinUrl)

        val newId = StudyGroups.insert {
            it[StudyGroups.groupName] = groupName
            it[StudyGroups.description] = description
            it[StudyGroups.dateCreated] = currentTime
            it[StudyGroups.qrCodeUrl] = generatedUrl
            it[StudyGroups.qrCodeData] = generatedQrData
        } get StudyGroups.id // Returns the generated ID

        StudyGroupEntity(
            id = newId,
            groupName = groupName,
            description = description,
            dateCreated = currentTime,
            qrCodeUrl = generatedUrl,
            qrCodeData = generatedQrData
        )
    }

    // Read all study groups
    fun getAllStudyGroups(): List<StudyGroupEntity> = transaction {
        StudyGroups.selectAll().map(::rowToStudyGroupEntity)
    }

    // Get a study group given its id
    fun getStudyGroupById(id: Long): StudyGroupEntity? = transaction {
        StudyGroups
            .selectAll()
            .andWhere { StudyGroups.id eq id }
            .map(::rowToStudyGroupEntity)
            .singleOrNull()
    }

    // Update a study group – only allows changes to groupName and description
    fun updateStudyGroup(id: Long, groupName: String, description: String): Boolean = transaction {
        StudyGroups.update({ StudyGroups.id eq id }) {
            it[StudyGroups.groupName] = groupName
            it[StudyGroups.description] = description
            // Does not update dateCreated, qr code url, or qr code data
        } > 0
    }

    // Delete a study group
    fun deleteStudyGroup(id: Long): Boolean = transaction {
        StudyGroups.deleteWhere { StudyGroups.id eq id } > 0
    }
}
