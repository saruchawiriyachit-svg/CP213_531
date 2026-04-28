package com.example.project.feature.decision.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DecidrDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDecision(decision: DecisionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOptions(options: List<OptionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFactors(factors: List<FactorEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: HistoryEntity)

    @Transaction
    @Query("SELECT * FROM decisions WHERE id = :decisionId")
    fun getDecisionWithDetails(decisionId: String): Flow<DecisionWithDetails?>

    @Transaction
    @Query("SELECT * FROM decisions ORDER BY timestamp DESC")
    fun getAllDecisionsWithDetails(): Flow<List<DecisionWithDetails>>

    @Query("DELETE FROM decisions WHERE id = :decisionId")
    suspend fun deleteDecisionById(decisionId: String)

    @Query("UPDATE history SET isLiked = :isLiked WHERE historyId = :historyId")
    suspend fun updateFeedback(historyId: Long, isLiked: Boolean)
}
