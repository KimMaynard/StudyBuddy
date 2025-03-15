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

    // Create
    // = transaction used to return the value of its last expression
    fun createClass(classEntity: ClassEntity): ClassEntity = transaction {
        val newId = Classes.insert {
            it[className] = classEntity.className
            it[classCode] = classEntity.classCode
        } get Classes.id // Returns the generated ID
        classEntity.copy(id = newId) // classEntity.id would be null, so we need to copy the new ID instead
    }

    // Read all
    fun getAllClasses(): List<ClassEntity> = transaction {
        Classes.selectAll().map(::rowToClassEntity)
    }



    // Get a class given its id
    // Renamed to getClassById to maintain consistency with previous function name
    fun getClassById(id: Long): ClassEntity? = transaction {
        // Condition necessary for the SqlExpressionBuilder
        // Necessary for the lambda expression - was giving a type mismatch error, stating that it was
        // seeing the () -> Op<Boolean> datatype when it required List<Expression<*>>
        val condition = SqlExpressionBuilder.run {
            Classes.id eq id
        }

        // Select the condition via select(condition)
        // Needed to prevent a type mismatch
        Classes.select(condition)
            .map { row -> rowToClassEntity(row) } // Now able to map the row to a class entity
            .singleOrNull()
    }

    // Update
    fun updateClass(id: Long, updatedClass: ClassEntity): Boolean = transaction {
        Classes.update({ Classes.id eq id }) {
            it[className] = updatedClass.className
            it[classCode] = updatedClass.classCode
        } > 0
    }

    // Delete
    fun deleteClass(id: Long): Boolean = transaction {
        Classes.deleteWhere { Classes.id eq id } > 0
    }
}