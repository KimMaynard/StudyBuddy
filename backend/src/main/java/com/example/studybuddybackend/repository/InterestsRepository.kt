package com.example.studybuddybackend.repository

import com.example.studybuddybackend.database.entities.Interests
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

data class InterestEntity(
    val id: Long? = null,
    val name: String
)

private fun rowToInterestEntity(row: ResultRow): InterestEntity{
    return InterestEntity(
        id = row[Interests.id],
        name = row[Interests.name]
    )
}

class InterestsRepository {

    // Create a new interest
    fun createInterest(interestEntity: InterestEntity): InterestEntity = transaction {
        val newId = Interests.insert {
            it[name] = interestEntity.name
        } get Interests.id // Returns the generated ID
        interestEntity.copy(id = newId)
    }

    // Read all interests
    fun getAllInterests(): List<InterestEntity> = transaction {
        Interests.selectAll().map(::rowToInterestEntity)
    }

    // Get an interest by its id
    fun getInterestById(id: Long): InterestEntity? = transaction {
        Interests.selectAll()
            .andWhere { Interests.id eq id }
            .map(::rowToInterestEntity)
            .singleOrNull()
    }

    // Update interest info
    fun updateInterest(id: Long, updatedInterest: InterestEntity): Boolean = transaction {
        Interests.update({Interests.id eq id  }){
            it[name] = updatedInterest.name
        } > 0
    }

    // Delete an interest
    fun deleteInterest(id: Long): Boolean = transaction {
        Interests.deleteWhere { Interests.id eq id } > 0
    }




}