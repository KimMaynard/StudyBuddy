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


    //Create
    fun createInterest(interestEntity: InterestEntity): InterestEntity = transaction {
        val newId = Interests.insert {
            it[name] = interestEntity.name
        } get Interests.id // Returns the generated ID
        interestEntity.copy(id = newId)
    }

    //Read all
    fun getAllInterests(): List<InterestEntity> = transaction {
        Interests.selectAll().map(::rowToInterestEntity)
    }

    // Get an interest given its id
    fun getInterestById(id: Long): InterestEntity? = transaction {

        val condition = SqlExpressionBuilder.run {
            Interests.id eq id
        }

        Interests.select(condition)
            .map { row -> rowToInterestEntity(row) } // Now able to map the row to a major entity
            .singleOrNull()
    }

    fun updateInterest(id: Long, updatedInterest: InterestEntity): Boolean = transaction {
        Interests.update({Interests.id eq id  }){
            it[name] = updatedInterest.name
        } > 0
    }

    //Delete
    fun deleteInterest(id: Long): Boolean = transaction {
        Interests.deleteWhere { Interests.id eq id } > 0
    }



}