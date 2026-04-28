package com.example.project.feature.decision.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "history",
    foreignKeys = [
        ForeignKey(
            entity = DecisionEntity::class,
            parentColumns = ["id"],
            childColumns = ["decisionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("decisionId")]
)
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val historyId: Long = 0,
    val decisionId: String,
    val chosenOptionId: String,
    val confidenceScore: Float,
    val reasoning: String,
    val completedAt: Long,
    val isLiked: Boolean? = null
)
