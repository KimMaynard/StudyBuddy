package com.example.studybuddybackend.repository

import com.example.studybuddybackend.database.entities.Classes
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

// Data class for ClassEntity
data class ClassEntity(
    val id: Long? = null,
    val className: String,
    val classCode: String
)

// Map result row to ClassEntity
private fun rowToClassEntity(row: ResultRow): ClassEntity {
    return ClassEntity(
        id = row[Classes.id],
        className = row[Classes.className],
        classCode = row[Classes.classCode]
    )
}

object ClassesRepository {

    // Create a new class
    // = used to return the value of its last expression
    fun createClass(classEntity: ClassEntity): ClassEntity = transaction {
        val newId = Classes.insert {
            it[className] = classEntity.className
            it[classCode] = classEntity.classCode
        } get Classes.id // Returns the generated ID
        classEntity.copy(id = newId) // classEntity.id would be null, so we need to copy the new ID instead
    }

    // Read all classes
    fun getAllClasses(): List<ClassEntity> = transaction {
        Classes.selectAll().map(::rowToClassEntity)
    }

    // Get a class given its id
    fun getClassById(id: Long): ClassEntity? = transaction {
        Classes.selectAll()
            .andWhere { Classes.id eq id }
            .map(::rowToClassEntity)
            .singleOrNull()
    }

    // Update a class
    fun updateClass(id: Long, updatedClass: ClassEntity): Boolean = transaction {
        Classes.update({ Classes.id eq id }) {
            it[className] = updatedClass.className
            it[classCode] = updatedClass.classCode
        } > 0
    }

    // Delete a class
    fun deleteClass(id: Long): Boolean = transaction {
        Classes.deleteWhere { Classes.id eq id } > 0
    }
}