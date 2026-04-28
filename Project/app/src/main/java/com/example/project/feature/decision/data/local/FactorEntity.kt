package com.example.project.feature.decision.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "factors",
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
data class FactorEntity(
    @PrimaryKey val id: String,
    val decisionId: String,
    val name: String,
    val weight: Float
)
