package com.example.project.ai.domain

import com.example.project.core.error.Resource
import com.example.project.feature.chat.domain.ChatMessage

interface AiChatRepository {
    suspend fun sendMessage(
        context: String,
        history: List<ChatMessage>,
        message: String
    ): Resource<ChatMessage>
}
