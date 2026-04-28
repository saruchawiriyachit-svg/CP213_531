package com.example.project.feature.chat.domain

import androidx.compose.runtime.Immutable

@Immutable
data class ChatMessage(
    val id: String,
    val text: String,
    val isFromJelly: Boolean
)
