package com.example.studybuddybackend.repository

import com.example.studybuddybackend.database.entities.StudentUserInterests
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq as exposedEq
import java.time.OffsetDateTime

data class StudentUserInterestEntity(
    val userId: Long,
    val interestId: Long,

    )


class StudentUserInterestsRepository{


    // insert interest into table
    fun createStudentUserInterest(userId: Long, interestId: Long): StudentUserInterestEntity? = transaction {
        val inserted = StudentUserInterests.insert {
            it[this.userId] = userId
            it[this.interestId] = interestId
        }
        val vals = inserted.resultedValues
        if (!vals.isNullOrEmpty()){
            val row = vals.first()
            StudentUserInterestEntity(
                userId = row[StudentUserInterests.userId],
                interestId = row[StudentUserInterests.interestId]
            )
        }else{
            null
        }
    }
    // finds all users with the specified interest ID
    fun getAllStudentByInterestId(interestId: Long): List<StudentUserInterestEntity> = transaction {
        StudentUserInterests.selectAll()
            .andWhere { StudentUserInterests.interestId exposedEq interestId }
            .map{row->
                StudentUserInterestEntity(
                    userId = row[StudentUserInterests.userId],
                    interestId = row[StudentUserInterests.interestId]
                )
            }
    }

    // finds all interests for a specified user ID
    fun getAllInterestsByUserId(userId: Long): List<StudentUserInterestEntity> = transaction {
        StudentUserInterests.selectAll()
            .andWhere { StudentUserInterests.userId exposedEq userId }
            .map{row->
                StudentUserInterestEntity(
                    userId = row[StudentUserInterests.userId],
                    interestId = row[StudentUserInterests.interestId]
                )
            }
    }

    fun deleteStudentUserInterest(userId: Long, interestId: Long): Boolean = transaction {
        StudentUserInterests.deleteWhere { (StudentUserInterests.userId exposedEq userId) and
                (StudentUserInterests.interestId exposedEq interestId) }
    } > 0
}