package com.example.project.ai.data

import com.example.project.ai.data.remote.AiApiService
import com.example.project.ai.data.remote.AiDecidrRequest
import com.example.project.ai.domain.AiRecommendationRepository
import com.example.project.core.error.AppError
import com.example.project.core.error.Resource
import com.example.project.feature.decision.domain.Decision
import com.example.project.feature.decision.domain.ProsCons
import com.example.project.feature.decision.domain.Recommendation
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException

class NetworkAiRecommendationRepository(
    private val apiService: AiApiService
) : AiRecommendationRepository {

    override suspend fun getRecommendation(decision: Decision): Resource<Recommendation> {
        return try {
            // Apply a 15-second timeout for the AI network call
            withTimeout(15_000L) {
                // Map Domain models to Request DTO
                val request = AiDecidrRequest(
                    question = "Help me decide: ${decision.query}",
                    options = decision.options.map { it.title },
                    factors = decision.factors.map { it.name }
                )

                // Make API Call
                val response = apiService.fetchRecommendation(request)

                // Map Response DTO back to Domain model
                val mappedProsCons = response.optionsAnalysis.mapValues { entry ->
                    ProsCons(score = entry.value.score, pros = entry.value.pros, cons = entry.value.cons)
                }

                val recommendation = Recommendation(
                    recommendedOptionId = response.recommendedOptionId,
                    reasoning = response.explanation,
                    confidenceScore = response.confidenceScore,
                    prosAndCons = mappedProsCons
                )

                Resource.Success(recommendation)
            }
        } catch (e: IOException) {
            Resource.Error(AppError.NetworkError)
        } catch (e: HttpException) {
            Resource.Error(AppError.AiServiceUnavailable)
        } catch (e: Exception) {
            // Catch timeouts (TimeoutCancellationException) and unexpected errors
            Resource.Error(AppError.UnknownError(e.message))
        }
    }
}
