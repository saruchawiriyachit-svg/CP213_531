package com.example.project.ai.data

import com.example.project.ai.data.remote.GeminiApiService
import com.example.project.ai.data.remote.GeminiContent
import com.example.project.ai.data.remote.GeminiPart
import com.example.project.ai.data.remote.GeminiRequest
import com.example.project.ai.domain.AiChatRepository
import com.example.project.core.error.AppError
import com.example.project.core.error.Resource
import com.example.project.feature.chat.domain.ChatMessage
import java.util.UUID

class GeminiAiChatRepository(
    private val geminiApiService: GeminiApiService,
    private val apiKey: String
) : AiChatRepository {

    override suspend fun sendMessage(
        context: String,
        history: List<ChatMessage>,
        message: String
    ): Resource<ChatMessage> {
        return try {
            // Build system context as first user turn
            val systemPrompt = buildString {
                appendLine("You are Jelly, a friendly and cute golden retriever AI assistant inside the Decidr app.")
                appendLine("Your job is to help users think through their decisions in a warm, helpful, and concise way.")
                appendLine("Always respond in the same language the user is using (Thai or English).")
                appendLine("Keep answers short (2-4 sentences max) and conversational. Add a dog emoji 🐶 or paw 🐾 occasionally.")
                if (context.isNotBlank()) {
                    appendLine()
                    appendLine("Decision context: $context")
                }
            }

            // Map chat history to Gemini alternating turns
            // Gemini requires strictly alternating user/model roles
            val historyContents = mutableListOf<GeminiContent>()

            // Add system prompt as first user message
            historyContents.add(
                GeminiContent(role = "user", parts = listOf(GeminiPart(systemPrompt)))
            )
            historyContents.add(
                GeminiContent(role = "model", parts = listOf(GeminiPart("Woof! Got it, I'm ready to help! 🐶")))
            )

            // Add actual conversation history (skip first greeting from Jelly which isn't in real history)
            history.filter { !it.isFromJelly || history.indexOf(it) > 0 }.forEach { msg ->
                historyContents.add(
                    GeminiContent(
                        role  = if (msg.isFromJelly) "model" else "user",
                        parts = listOf(GeminiPart(msg.text))
                    )
                )
            }

            // Add the new user message
            historyContents.add(
                GeminiContent(role = "user", parts = listOf(GeminiPart(message)))
            )

            val request  = GeminiRequest(contents = historyContents)
            val response = geminiApiService.generateContent(apiKey = apiKey, request = request)

            val replyText = response.candidates
                ?.firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text
                ?: "Woof... I couldn't think of an answer right now. Try again? 🐾"

            Resource.Success(
                ChatMessage(
                    id         = UUID.randomUUID().toString(),
                    text       = replyText.trim(),
                    isFromJelly = true
                )
            )
        } catch (e: Exception) {
            Resource.Error(AppError.UnknownError(e.message))
        }
    }
}
