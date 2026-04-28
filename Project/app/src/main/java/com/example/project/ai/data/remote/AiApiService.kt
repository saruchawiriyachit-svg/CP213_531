package com.example.project.ai.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface AiApiService {
    @POST("v1/decide")
    suspend fun fetchRecommendation(@Body request: AiDecidrRequest): AiDecidrResponse

    @POST("v1/chat")
    suspend fun sendChatMessage(@Body request: AiChatRequest): AiChatResponse

    @POST("v1/feedback")
    suspend fun submitFeedback(@Body request: AiFeedbackRequest)
}
