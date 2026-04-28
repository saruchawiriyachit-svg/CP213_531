package com.example.project.ai.domain.usecase

import com.example.project.ai.domain.AiRecommendationRepository
import com.example.project.core.error.Resource
import com.example.project.feature.decision.domain.Decision
import com.example.project.feature.decision.domain.Recommendation

/**
 * Use case enforcing Clean Architecture to get an AI recommendation.
 */
class GetAiRecommendationUseCase(
    private val repository: AiRecommendationRepository
) {
    suspend operator fun invoke(decision: Decision): Resource<Recommendation> {
        if (decision.options.isEmpty()) {
            return Resource.Error(com.example.project.core.error.AppError.UnknownError("No options provided"))
        }
        
        return repository.getRecommendation(decision)
    }
}
