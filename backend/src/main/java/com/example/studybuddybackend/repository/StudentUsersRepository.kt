package com.example.studybuddybackend.repository

import com.example.studybuddybackend.database.entities.StudentUsers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.OffsetDateTime

data class StudentUserEntity(
    val id: Long? = null,
    val firstName: String,
    val middleName: String? = "", // Given a placeholder to prevent errors
    val lastName: String,
    val profilePicture: ByteArray?,
    val username: String,
    val email: String,
    val areaCode: String? = "", // Given a placeholder to prevent errors
    val phoneNumber: String? = "", // Given a placeholder to prevent errors
    val password: String,
    val currentDegree: String,
    val seniority: String,
    val preferredStudyStyle: String,
    val dateCreated: OffsetDateTime
)

private fun rowToStudentUserEntity(row: ResultRow): StudentUserEntity{
    return StudentUserEntity(
        id = row[StudentUsers.id],
        firstName = row[StudentUsers.firstName],
        middleName = row[StudentUsers.middleName],
        lastName = row[StudentUsers.lastName],
        profilePicture = row[StudentUsers.profilePicture],
        username = row[StudentUsers.username],
        email = row[StudentUsers.email],
        areaCode = row[StudentUsers.areaCode],
        phoneNumber = row[StudentUsers.phoneNumber],
        password = row[StudentUsers.password],
        currentDegree = row[StudentUsers.currentDegree],
        seniority = row[StudentUsers.seniority],
        preferredStudyStyle = row[StudentUsers.preferredStudyStyle],
        dateCreated = row[StudentUsers.dateCreated]
    )
}

class StudentUsersRepository {

    // Create
    fun createStudentUser(studentUserEntity: StudentUserEntity): StudentUserEntity = transaction {
        val newId = StudentUsers.insert {
            it[firstName] = studentUserEntity.firstName
            it[middleName] = studentUserEntity.middleName
            it[lastName] = studentUserEntity.lastName
            it[profilePicture] = studentUserEntity.profilePicture
            it[username] = studentUserEntity.username
            it[email] = studentUserEntity.email
            it[areaCode] = studentUserEntity.areaCode
            it[phoneNumber] = studentUserEntity.phoneNumber
            it[password] = studentUserEntity.password
            it[currentDegree] = studentUserEntity.currentDegree
            it[seniority] = studentUserEntity.seniority
            it[preferredStudyStyle] = studentUserEntity.preferredStudyStyle
            it[dateCreated] = studentUserEntity.dateCreated
        } get StudentUsers.id // Returns the generated ID
        studentUserEntity.copy(id = newId) // classEntity.id would be null, so we need to copy the new ID instead
    }

    // Gets all student users
    fun getAllStudentUsers(): List<StudentUserEntity> = transaction {
        StudentUsers.selectAll().map(::rowToStudentUserEntity)
    }

    // Get a student user given its id
    fun getStudentUserById(id: Long): StudentUserEntity? = transaction {
        StudentUsers
            .selectAll()                    // SELECT * FROM student_users
            .andWhere { StudentUsers.id eq id }  // WHERE student_users.id = id
            .map(::rowToStudentUserEntity)
            .singleOrNull()
    } // select() would cause type mismatches. Use this workaround.

    // Update
    fun updateStudentUser(id: Long, updatedStudentUser: StudentUserEntity): Boolean = transaction {
        StudentUsers.update({StudentUsers.id eq id  }){
            it[firstName] = updatedStudentUser.firstName
            it[middleName] = updatedStudentUser.middleName
            it[lastName] = updatedStudentUser.lastName
            it[profilePicture] = updatedStudentUser.profilePicture
            it[username] = updatedStudentUser.username
            it[email] = updatedStudentUser.email
            it[areaCode] = updatedStudentUser.areaCode
            it[phoneNumber] = updatedStudentUser.phoneNumber
            it[password] = updatedStudentUser.password
            it[currentDegree] = updatedStudentUser.currentDegree
            it[seniority] = updatedStudentUser.seniority
            it[preferredStudyStyle] = updatedStudentUser.preferredStudyStyle
            it[dateCreated] = updatedStudentUser.dateCreated
        } > 0
    }

    // Delete
    fun deleteStudentUser(id: Long): Boolean = transaction {
        StudentUsers.deleteWhere { StudentUsers.id eq id } > 0
    }

}