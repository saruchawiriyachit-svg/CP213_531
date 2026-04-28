package com.example.project.feature.decision.ui

import com.example.project.feature.decision.domain.Decision
import com.example.project.feature.decision.domain.Recommendation

enum class MascotState {
    IDLE,
    THINKING,
    EXCITED,
    CONFUSED,
    SAD
}

data class DecisionUiState(
    val isLoading: Boolean = false,
    val currentDecision: Decision? = null,
    val aiRecommendation: Recommendation? = null,
    val mascotState: MascotState = MascotState.IDLE,
    val errorMessage: String? = null
)
