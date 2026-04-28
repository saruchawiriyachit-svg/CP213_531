package com.example.project.feature.decision.domain

data class Decision(
    val id: String,
    val query: String,
    val options: List<Option>,
    val factors: List<Factor>
)

data class Option(
    val id: String,
    val title: String,
    val description: String = ""
)

data class Factor(
    val id: String,
    val name: String,
    val weight: Float // Typically 1.0 to 10.0 or 0.0 to 1.0
)

data class DecisionHistoryItem(
    val decision: Decision,
    val recommendedOptionTitle: String,
    val reasoning: String,
    val timestamp: Long
)
