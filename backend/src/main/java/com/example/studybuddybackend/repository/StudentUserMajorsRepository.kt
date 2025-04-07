package com.example.studybuddybackend.repository

import com.example.studybuddybackend.database.entities.StudentUserMajors
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

// 1) Alias eq to avoid overshadowing
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq as exposedEq
// 2) Alias deleteWhere to avoid overshadowing
import org.jetbrains.exposed.sql.deleteWhere as exposedDeleteWhere

/**
 * Data class representing a row in the bridging table (student_user_majors).
 * userId references StudentUsers.id, majorId references Majors.id.
 */
data class UserMajorAssociation(
    val userId: Long,
    val majorId: Long
)

class StudentUserMajorsRepository {

    /**
     * Retrieves all (userId, majorId) associations for the given user.
     * Uses selectAll().andWhere plus the aliased eq to avoid overshadowing.
     */
    fun getAssociationsForUser(userId: Long): List<UserMajorAssociation> = transaction {
        StudentUserMajors
            .selectAll()
            .andWhere { StudentUserMajors.userId exposedEq userId }
            .map { row ->
                UserMajorAssociation(
                    userId = row[StudentUserMajors.userId],
                    majorId = row[StudentUserMajors.majorId]
                )
            }
    }

    /**
     * Inserts a row into student_user_majors, returning the new association or null if it fails.
     */
    fun addMajorToStudent(userId: Long, majorId: Long): UserMajorAssociation? = transaction {
        val inserted = StudentUserMajors.insert {
            it[this.userId] = userId
            it[this.majorId] = majorId
        }
        val vals = inserted.resultedValues
        if (!vals.isNullOrEmpty()) {
            val row = vals.first()
            UserMajorAssociation(
                userId = row[StudentUserMajors.userId],
                majorId = row[StudentUserMajors.majorId]
            )
        } else {
            null
        }
    }

    /**
     * Removes an association between a user and a major.
     * Returns true if at least one row was deleted, false otherwise.
     */
    fun removeMajorFromStudent(userId: Long, majorId: Long): Boolean = transaction {
        val rowsDeleted = StudentUserMajors.exposedDeleteWhere {
            (StudentUserMajors.userId exposedEq userId) and
                    (StudentUserMajors.majorId exposedEq majorId)
        }
        rowsDeleted > 0
    }

}
