package com.example.studybuddybackend.repository



import com.example.studybuddybackend.database.entities.StudyGroupMembers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq as exposedEq
import java.time.OffsetDateTime

data class StudyGroupMemberEntity(
    val groupId: Long,
    val userId: Long,
    val joinedAt: OffsetDateTime
)

private fun rowToStudyGroupMemberEntity(row: ResultRow): StudyGroupMemberEntity{
    return StudyGroupMemberEntity(
        groupId = row[StudyGroupMembers.groupId],
        userId = row[StudyGroupMembers.userId],
        joinedAt = row[StudyGroupMembers.joinedAt]
    )
}

class StudyGroupMembersRepository{

    fun createStudyGroupMember(groupId: Long, userId: Long): StudyGroupMemberEntity? = transaction {
        val inserted = StudyGroupMembers.insert {
            it[this.groupId] = groupId
            it[this.userId] = userId
            it[this.joinedAt] = OffsetDateTime.now()
        }
        val vals = inserted.resultedValues
        if (!vals.isNullOrEmpty()){
            val row = vals.first()
            StudyGroupMemberEntity(
                groupId = row[StudyGroupMembers.groupId],
                userId = row[StudyGroupMembers.userId],
                joinedAt = row[StudyGroupMembers.joinedAt]
            )
        }else{
            null
        }
    }

    fun getAllStudyGroupMembers(groupId: Long): List<StudyGroupMemberEntity> = transaction {
        StudyGroupMembers.selectAll()
            .andWhere { StudyGroupMembers.groupId exposedEq groupId }
            .map{row->
                StudyGroupMemberEntity(
                    groupId = row[StudyGroupMembers.groupId],
                    userId = row[StudyGroupMembers.userId],
                    joinedAt = row[StudyGroupMembers.joinedAt]
                )
            }
    }

    // finds all the groups a user is apart of
    // commented out for review of logic
    fun getAllMembersStudyGroups(userId: Long): List<StudyGroupMemberEntity> = transaction {
        StudyGroupMembers.selectAll()
            .andWhere { StudyGroupMembers.userId exposedEq userId }
            .map{row->
                StudyGroupMemberEntity(
                    groupId = row[StudyGroupMembers.groupId],
                    userId = row[StudyGroupMembers.userId],
                    joinedAt = row[StudyGroupMembers.joinedAt]
                )
            }
    }



    fun deleteChatRoomMember(groupId: Long, userId: Long): Boolean = transaction {
        StudyGroupMembers.deleteWhere { (StudyGroupMembers.groupId exposedEq groupId) and
                (StudyGroupMembers.userId exposedEq userId) }
    } > 0
}