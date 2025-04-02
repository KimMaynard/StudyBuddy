package com.example.studybuddybackend.repository

import com.example.studybuddybackend.database.entities.StudyGroups
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.OffsetDateTime

data class StudyGroupEntity(
    val id: Long? = null,
    val groupName: String,
    val description: String,
    val dateCreated: OffsetDateTime,
    val qrCodeUrl: String?,
    val qrCodeData: ByteArray?
)

private fun rowToStudyGroupEntity(row: ResultRow): StudyGroupEntity{
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

    // Create a study group
    fun createStudyGroup(studyGroupEntity: StudyGroupEntity): StudyGroupEntity = transaction {
        val newId = StudyGroups.insert {
            it[groupName] = studyGroupEntity.groupName
            it[description] = studyGroupEntity.description
            it[dateCreated] = studyGroupEntity.dateCreated
            it[qrCodeUrl] = studyGroupEntity.qrCodeUrl
            it[qrCodeData] = studyGroupEntity.qrCodeData
        } get StudyGroups.id // Returns the generated ID
        studyGroupEntity.copy(id = newId) // classEntity.id would be null, so we need to copy the new ID instead
    }

    // Read all study groups
    fun getAllStudyGroups(): List<StudyGroupEntity> = transaction {
        StudyGroups.selectAll().map(::rowToStudyGroupEntity)
    }

    // Get a study group given its id
    fun getStudyGroupById(id: Long): StudyGroupEntity? = transaction {
        StudyGroups.selectAll()
            .andWhere { StudyGroups.id eq id }
            .map(::rowToStudyGroupEntity)
            .singleOrNull()
    }

    // Update a study group
    fun updateStudyGroup(id: Long, updatedStudyGroup: StudyGroupEntity): Boolean = transaction {
        StudyGroups.update({StudyGroups.id eq id  }){
            it[groupName] = updatedStudyGroup.groupName
            it[description] = updatedStudyGroup.description
            it[dateCreated] = updatedStudyGroup.dateCreated
            it[qrCodeUrl] = updatedStudyGroup.qrCodeUrl
            it[qrCodeData] = updatedStudyGroup.qrCodeData
        } > 0
    }

    // Delete a study group
    fun deleteStudyGroup(id: Long): Boolean = transaction {
        StudyGroups.deleteWhere { StudyGroups.id eq id } > 0
    }

}