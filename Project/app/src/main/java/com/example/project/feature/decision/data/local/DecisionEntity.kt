package com.example.project.feature.decision.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decisions")
data class DecisionEntity(
    @PrimaryKey val id: String,
    val query: String,
    val timestamp: Long
)
