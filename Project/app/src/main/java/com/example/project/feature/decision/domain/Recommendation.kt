package com.example.project.feature.decision.domain

import androidx.compose.runtime.Immutable

@Immutable
data class Recommendation(
    val recommendedOptionId: String,
    val reasoning: String,
    val confidenceScore: Float,
    val prosAndCons: Map<String, ProsCons> = emptyMap()
)

@Immutable
data class ProsCons(
    val score: Int = 0,
    val pros: List<String>,
    val cons: List<String>
)
