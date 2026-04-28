package com.example.project.ai.data

import com.example.project.ai.domain.AiChatRepository
import com.example.project.core.error.Resource
import com.example.project.feature.chat.domain.ChatMessage
import java.util.UUID

/**
 * A local mock implementation of AiChatRepository.
 * Responds with context-aware replies based on keywords in the user's message.
 * Used until a real AI backend is connected.
 */
class MockAiChatRepository : AiChatRepository {

    override suspend fun sendMessage(
        context: String,
        history: List<ChatMessage>,
        message: String
    ): Resource<ChatMessage> {
        // Simulate a short thinking delay
        kotlinx.coroutines.delay(800)

        val lowerMsg = message.lowercase()

        val reply = when {
            lowerMsg.contains("why") || lowerMsg.contains("ทำไม") ->
                "Great question! $context Based on the factors you set, the top option scores highest overall. 🐶"

            lowerMsg.contains("better") || lowerMsg.contains("difference") || lowerMsg.contains("ดีกว่า") ->
                "Each option has its own strengths! The winner edges ahead mainly because it balances all your factors better. Want me to dig deeper into any specific factor? 🔍"

            lowerMsg.contains("pros") || lowerMsg.contains("good") || lowerMsg.contains("ข้อดี") ->
                "The top pick's biggest pros are its strong performance across every factor you care about. It's not perfect, but it wins overall! ✅"

            lowerMsg.contains("cons") || lowerMsg.contains("bad") || lowerMsg.contains("ข้อเสีย") ->
                "No option is flawless! The main trade-off is that the winner might not score 100% on a single factor — but it's consistently solid across all of them. ⚠️"

            lowerMsg.contains("sure") || lowerMsg.contains("confident") || lowerMsg.contains("มั่นใจ") ->
                "I'm pretty confident! My recommendation is based on the weights you assigned to each factor. The higher the confidence score, the more decisive the result. 💪"

            lowerMsg.contains("other") || lowerMsg.contains("second") || lowerMsg.contains("ตัวเลือกอื่น") ->
                "The runner-up is also a solid choice! If the top pick doesn't feel right for some reason, the second option might suit you better in specific situations. Think it over! 🤔"

            lowerMsg.contains("help") || lowerMsg.contains("ช่วย") ->
                "Of course! You can ask me things like: why did you pick this? What are the pros and cons? How confident are you? I'm here to help you decide! 🐶"

            lowerMsg.contains("thank") || lowerMsg.contains("ขอบคุณ") || lowerMsg.contains("thanks") ->
                "You're welcome! Happy to help. If you change your mind or want to reconsider, just come back and we'll figure it out together! 🐾"

            lowerMsg.contains("hello") || lowerMsg.contains("hi") || lowerMsg.contains("สวัสดี") ->
                "Woof! 👋 I'm Jelly, your decision buddy! I've already looked at your options. What would you like to know? 🐶"

            else ->
                "That's a thoughtful question! Based on what I know about your decision, I'd say trust the data — but ultimately the choice is yours. Want me to explain any part of my analysis? 🐾"
        }

        return Resource.Success(
            ChatMessage(
                id = UUID.randomUUID().toString(),
                text = reply,
                isFromJelly = true
            )
        )
    }
}
