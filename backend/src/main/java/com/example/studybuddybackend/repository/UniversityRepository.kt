package com.example.studybuddybackend.repository


import com.example.studybuddybackend.database.entities.Universities
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

data class UniversityEntity(
    val id: Long? = null,
    val name: String,
    val address: String,
    val website: String,
)

private fun rowToUniversityEntity(row: ResultRow): UniversityEntity{
    return UniversityEntity(
        id = row[Universities.id],
        name = row[Universities.name],
        address = row[Universities.address],
        website = row[Universities.website]
    )
}
class UniversityRepository {

    //Create
    fun createUniversity(universityEntity: UniversityEntity): UniversityEntity = transaction {
        val newId = Universities.insert {
            it[name] = universityEntity.name
            it[address] = universityEntity.address
            it[website] = universityEntity.website
        } get Universities.id // Returns the generated ID
        universityEntity.copy(id = newId) // classEntity.id would be null, so we need to copy the new ID instead
    }

    // Read all
    fun getAllUniversities(): List<UniversityEntity> = transaction {
        Universities.selectAll().map(::rowToUniversityEntity)
    }

    // Get a university given its id
    fun getUniversityById(id: Long): UniversityEntity? = transaction {

        val condition = SqlExpressionBuilder.run {
            Universities.id eq id
        }

        Universities.select(condition)
            .map { row -> rowToUniversityEntity(row) } // Now able to map the row to a room entity
            .singleOrNull()
    }


    //Update
    fun updateUniversity(id: Long, updatedUniversity: UniversityEntity): Boolean = transaction {
        Universities.update({Universities.id eq id  }){
            it[name] = updatedUniversity.name
            it[address] = updatedUniversity.address
            it[website] = updatedUniversity.website
        } > 0
    }

    //Delete
    fun deleteUniversity(id: Long): Boolean = transaction {
        Universities.deleteWhere { Universities.id eq id } > 0
    }


}