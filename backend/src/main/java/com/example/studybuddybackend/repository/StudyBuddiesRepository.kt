package com.example.studybuddybackend.repository

import com.example.studybuddybackend.database.entities.StudyBuddies
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

// Alias eq and deleteWhere for avoiding overshadowing
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq as exposedEq
import org.jetbrains.exposed.sql.deleteWhere as exposedDeleteWhere

data class BuddyPair(
    val userA: Long,
    val userB: Long
)

class StudyBuddiesRepository {

    // Add a new study_user relationship
    // Only 1 row is stored - no need for opposing StudentUser id pairs
    fun addBuddy(userId: Long, buddyId: Long): BuddyPair? = transaction {

        /*
            By making sure userA will store the lower id value, it ensures that even if
            a user with a larger id tries to become study buddies with a lower id user,
            there will be no need for two, opposite id pairs
        */
        val (a, b) = if (userId < buddyId) userId to buddyId else buddyId to userId

        val inserted = StudyBuddies.insert {
            it[this.userA] = a
            it[this.userB] = b
        }
        val vals = inserted.resultedValues
        if (!vals.isNullOrEmpty()) {
            val row = vals.first()
            BuddyPair(
                userA = row[StudyBuddies.userA],
                userB = row[StudyBuddies.userB]
            )
        } else {
            null
        }
    }

    // Gets all buddies for a user
    fun getBuddiesForUser(userId: Long): List<BuddyPair> = transaction {
        // Condition with eq - necessary to prevent type mismatch
        val condition: Op<Boolean> = SqlExpressionBuilder.run {
            (StudyBuddies.userA exposedEq userId) or (StudyBuddies.userB exposedEq userId)
        }

        // SELECT * FROM study_buddies WHERE user_a = ? OR user_b = ?
        StudyBuddies
            .selectAll()
            .andWhere { condition }
            .map { row ->
                BuddyPair(
                    userA = row[StudyBuddies.userA],
                    userB = row[StudyBuddies.userB]
                )
            }
    }

    // Remove a study buddy relationship
    fun removeBuddy(userId: Long, buddyId: Long): Boolean = transaction {
        // Reordering IDs
        val (a, b) = if (userId < buddyId) userId to buddyId else buddyId to userId

        val rowsDeleted = StudyBuddies.exposedDeleteWhere {
            (StudyBuddies.userA exposedEq a) and (StudyBuddies.userB exposedEq b)
        }
        rowsDeleted > 0
    }
}
