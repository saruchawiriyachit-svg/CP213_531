package com.example.project.ai.data.remote

data class AiDecidrRequest(
    val question: String,
    val options: List<String>,
    val factors: List<String>
)

data class AiDecidrResponse(
    val recommendedOptionId: String,
    val explanation: String,
    val confidenceScore: Float,
    val optionsAnalysis: Map<String, OptionAnalysisResponse>
)

data class OptionAnalysisResponse(
    val score: Int = 0,
    val pros: List<String>,
    val cons: List<String>
)

data class AiChatMessage(
    val role: String,
    val content: String
)

data class AiChatRequest(
    val context: String,
    val messages: List<AiChatMessage>,
    val newMessage: String
)

data class AiChatResponse(
    val reply: String
)

data class AiFeedbackRequest(
    val decisionId: String,
    val isLiked: Boolean
)
