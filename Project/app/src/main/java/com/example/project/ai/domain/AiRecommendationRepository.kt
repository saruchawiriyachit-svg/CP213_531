package com.example.project.ai.domain

import com.example.project.core.error.Resource
import com.example.project.feature.decision.domain.Decision
import com.example.project.feature.decision.domain.Recommendation

interface AiRecommendationRepository {
    suspend fun getRecommendation(decision: Decision): Resource<Recommendation>
}
