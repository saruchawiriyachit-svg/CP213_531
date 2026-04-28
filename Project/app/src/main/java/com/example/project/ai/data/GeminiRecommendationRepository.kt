package com.example.project.ai.data

import com.example.project.ai.data.remote.GeminiApiService
import com.example.project.ai.data.remote.GeminiContent
import com.example.project.ai.data.remote.GeminiPart
import com.example.project.ai.data.remote.GeminiRequest
import com.example.project.ai.domain.AiRecommendationRepository
import com.example.project.core.error.AppError
import com.example.project.core.error.Resource
import com.example.project.feature.decision.domain.Decision
import com.example.project.feature.decision.domain.ProsCons
import com.example.project.feature.decision.domain.Recommendation
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class GeminiRecommendationRepository(
    private val geminiApiService: GeminiApiService,
    private val apiKey: String
) : AiRecommendationRepository {

    override suspend fun getRecommendation(decision: Decision): Resource<Recommendation> {
        return try {
            val optionsList  = decision.options.joinToString("\n") { "- ${it.title}" }
            val factorsList  = if (decision.factors.isNotEmpty())
                decision.factors.joinToString(", ") { "${it.name} (importance: ${(it.weight * 10).toInt()}/10)" }
            else "general preference"

            val prompt = """
You are a smart decision-making assistant. Analyze these options for the question: "${decision.query}"

Options:
$optionsList

Decision factors: $factorsList

Respond ONLY in valid JSON (no markdown, no explanation outside JSON) and use the exact structure shown in this example:

{
  "recommendedOption": "Option 1 Name here",
  "reasoning": "This option is best because it matches your factor of cost perfectly.",
  "confidenceScore": 0.85,
  "analysis": {
    "Option 1 Name here": {
      "score": 90,
      "pros": ["Very affordable", "Good location"],
      "cons": ["Smaller size"]
    },
    "Option 2 Name here": {
      "score": 60,
      "pros": ["Large size"],
      "cons": ["Too expensive", "Bad location"]
    }
  }
}

Important: pros and cons must be real, specific insights about each option. The score must be an integer between 0 and 100. Use the same language as the question.
""".trimIndent()

            val request = GeminiRequest(
                contents = listOf(
                    GeminiContent(role = "user", parts = listOf(GeminiPart(prompt)))
                )
            )

            val response  = geminiApiService.generateContent(apiKey = apiKey, request = request)
            val rawText   = response.candidates
                ?.firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text
                ?: return Resource.Error(AppError.AiServiceUnavailable)

            parseRecommendation(rawText, decision)

        } catch (e: HttpException) {
            when (e.code()) {
                429 -> Resource.Error(AppError.RateLimitError)
                503, 502 -> Resource.Error(AppError.AiServiceUnavailable)
                else -> Resource.Error(AppError.UnknownError("HTTP ${e.code()}: ${e.message()}"))
            }
        } catch (e: IOException) {
            Resource.Error(AppError.NetworkError)
        } catch (e: Exception) {
            Resource.Error(AppError.UnknownError(e.message))
        }
    }

    private fun parseRecommendation(raw: String, decision: Decision): Resource<Recommendation> {
        return try {
            // Strip markdown code fences if present
            val cleaned = raw
                .replace("```json", "")
                .replace("```", "")
                .trim()

            val json           = JSONObject(cleaned)
            val recommendedName = json.optString("recommendedOption", decision.options.firstOrNull()?.title ?: "").trim()
            val reasoning      = json.optString("reasoning", "The AI provided an analysis for these options.")
            val confidence     = json.optDouble("confidenceScore", 0.5).toFloat().coerceIn(0f, 1f)
            val analysisJson   = json.optJSONObject("analysis") ?: JSONObject()

            // Map AI option names back to domain Option IDs
            val prosAndCons = decision.options.associate { option ->
                // Try exact match first, then case-insensitive
                val key = analysisJson.keys().asSequence()
                    .firstOrNull { it.equals(option.title, ignoreCase = true) }
                    ?: option.title

                val optionJson = if (analysisJson.has(key)) analysisJson.getJSONObject(key)
                                 else JSONObject()

                val score = optionJson.optInt("score", 0)

                val pros = optionJson.optJSONArray("pros")
                    ?.let { arr -> (0 until arr.length()).map { arr.getString(it) } }
                    ?: listOf("Good overall option")

                val cons = optionJson.optJSONArray("cons")
                    ?.let { arr -> (0 until arr.length()).map { arr.getString(it) } }
                    ?: listOf("Consider your priorities")

                option.id to ProsCons(score = score, pros = pros, cons = cons)
            }

            // Find the recommended option ID by name match
            val recommendedId = decision.options
                .firstOrNull { it.title.equals(recommendedName, ignoreCase = true) }
                ?.id
                ?: decision.options.first().id

            Resource.Success(
                Recommendation(
                    recommendedOptionId = recommendedId,
                    reasoning           = reasoning,
                    confidenceScore     = confidence,
                    prosAndCons         = prosAndCons
                )
            )
        } catch (e: Exception) {
            Resource.Error(AppError.UnknownError("Failed to parse AI response: ${e.message}"))
        }
    }
}
