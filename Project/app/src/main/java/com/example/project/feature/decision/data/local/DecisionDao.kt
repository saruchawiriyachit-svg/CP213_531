package com.example.project.feature.decision.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DecisionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDecision(decisionEntity: DecisionEntity)

    @Query("SELECT * FROM decisions WHERE id = :decisionId")
    fun getDecisionById(decisionId: String): Flow<DecisionEntity?>

    @Query("SELECT * FROM decisions")
    fun getAllDecisions(): Flow<List<DecisionEntity>>

    @Query("DELETE FROM decisions WHERE id = :decisionId")
    suspend fun deleteDecisionById(decisionId: String)
}
