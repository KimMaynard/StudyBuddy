package com.example.studybuddybackend.repository

import com.example.studybuddybackend.database.entities.Majors
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

data class MajorEntity(
    val id: Long? = null,
    val name: String
)

private fun rowToMajorEntity(row: ResultRow): MajorEntity{
    return MajorEntity(
        id = row[Majors.id],
        name = row[Majors.name]
    )
}


class MajorsRepository {

    //Create
    fun createMajor(majorEntity: MajorEntity): MajorEntity = transaction {
        val newId = Majors.insert {
            it[name] = majorEntity.name
        } get Majors.id // Returns the generated ID
        majorEntity.copy(id = newId)
    }

    //Read all
    fun getAllMajors(): List<MajorEntity> = transaction {
        Majors.selectAll().map(::rowToMajorEntity)
    }

    // Get a major given its id
    fun getMajorById(id: Long): MajorEntity? = transaction {
        Majors.selectAll()
            .andWhere { Majors.id eq id }
            .map(::rowToMajorEntity)
            .singleOrNull()
    }

    //Update
    fun updateMajor(id: Long, updatedMajor: MajorEntity): Boolean = transaction {
        Majors.update({Majors.id eq id  }){
            it[name] = updatedMajor.name
        } > 0
    }

    //Delete
    fun deleteMajor(id: Long): Boolean = transaction {
        Majors.deleteWhere { Majors.id eq id } > 0
    }






}