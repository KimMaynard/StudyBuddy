package com.example.studybuddybackend.repository

import com.example.studybuddybackend.database.entities.Universities
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

// Aliasing Exposed's eq and update to avoid overshadowing conflicts in updateUniversity()
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq as exposedEq
import org.jetbrains.exposed.sql.update as exposedUpdate

data class UniversityEntity(
    val id: Long? = null,
    val name: String,
    val address: String,
    val website: String,
    val userId: Long
)

private fun rowToUniversityEntity(row: ResultRow): UniversityEntity {
    return UniversityEntity(
        id = row[Universities.id],
        name = row[Universities.name],
        address = row[Universities.address],
        website = row[Universities.website],
        userId = row[Universities.userId]
    )
}

class UniversityRepository {

    // Create a new university record, returning the created entity.
    fun createUniversity(universityEntity: UniversityEntity): UniversityEntity = transaction {
        val newId = Universities.insert {
            it[name] = universityEntity.name
            it[address] = universityEntity.address
            it[website] = universityEntity.website
            it[userId] = universityEntity.userId
        } get Universities.id
        universityEntity.copy(id = newId)
    }

    // Get all university records from the table
    fun getAllUniversities(): List<UniversityEntity> = transaction {
        Universities.selectAll().map(::rowToUniversityEntity)
    }

    // Get a university by its ID
    // Uses a "selectAll + andWhere" approach to avoid overshadowing eq
    fun getUniversityById(id: Long): UniversityEntity? = transaction {
        Universities
            .selectAll()
            .andWhere { Universities.id exposedEq id } // Uses aliased eq
            .map(::rowToUniversityEntity)
            .singleOrNull()
    }


    // Update an existing university by ID
    // Used aliases "exposedUpdate" and "exposedEq" to avoid overshadowing issues
    fun updateUniversity(id: Long, updatedUniversity: UniversityEntity): Boolean = transaction {
        val rowsUpdated = Universities.exposedUpdate({ Universities.id exposedEq id }) {
            it[name] = updatedUniversity.name
            it[address] = updatedUniversity.address
            it[website] = updatedUniversity.website
            it[userId] = updatedUniversity.userId
        }
        rowsUpdated > 0
    }

    // Delete a university by ID
    fun deleteUniversity(id: Long): Boolean = transaction {
        val rowsDeleted = Universities.deleteWhere { Universities.id exposedEq id }
        rowsDeleted > 0
    }

}
