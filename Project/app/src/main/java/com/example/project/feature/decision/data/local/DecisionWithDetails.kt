package com.example.project.feature.decision.data.local

import androidx.room.Embedded
import androidx.room.Relation

data class DecisionWithDetails(
    @Embedded val decision: DecisionEntity,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "decisionId"
    )
    val options: List<OptionEntity>,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "decisionId"
    )
    val factors: List<FactorEntity>,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "decisionId"
    )
    val history: HistoryEntity?
)
