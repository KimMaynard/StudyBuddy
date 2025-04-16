package com.example.studybuddybackend.repository

import com.example.studybuddybackend.database.entities.StudentUserClasses
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq as exposedEq

data class StudentUserClassesEntity(
    val userId: Long,
    val classId: Long
)

class StudentUserClassesRepository {

    // Inserts a student user into a class
    fun createStudentUserClasses(userId: Long, classId: Long): StudentUserClassesEntity? = transaction {
        val inserted = StudentUserClasses.insert {
            it[this.userId] = userId
            it[this.classId] = classId
        }
        val vals = inserted.resultedValues
        if (!vals.isNullOrEmpty()) {
            val row = vals.first()
            StudentUserClassesEntity(
                userId = row[StudentUserClasses.userId],
                classId = row[StudentUserClasses.classId]
            )
        } else {
            null
        }
    }

    // Get all classes in a student user
    fun getAllClassesByUserId(userId: Long): List<StudentUserClassesEntity> = transaction {
        val query = StudentUserClasses.selectAll()
        query.andWhere { StudentUserClasses.userId exposedEq userId }
        query.map { row ->
            StudentUserClassesEntity(
                userId = row[StudentUserClasses.userId],
                classId = row[StudentUserClasses.classId]
            )
        }
    }

    // Gets all student users within a class
    fun getAllStudentByClassId(classId: Long): List<StudentUserClassesEntity> = transaction {
        val query = StudentUserClasses.selectAll()
        query.andWhere { StudentUserClasses.classId exposedEq classId }
        query.map { row ->
            StudentUserClassesEntity(
                userId = row[StudentUserClasses.userId],
                classId = row[StudentUserClasses.classId]
            )
        }
    }

    // Delete a student user from a class
    fun deleteStudentUserClass(userId: Long, classId: Long): Boolean = transaction {
        StudentUserClasses.deleteWhere {
            (StudentUserClasses.userId exposedEq userId) and (StudentUserClasses.classId exposedEq classId)
        } > 0
    }
}
