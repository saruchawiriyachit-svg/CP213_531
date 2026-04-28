package com.example.project.feature.chat.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.ai.domain.AiChatRepository
import com.example.project.core.error.Resource
import com.example.project.feature.chat.domain.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val decisionContext: String = ""
)

class ChatViewModel(
    private val aiChatRepository: AiChatRepository,
    initialContext: String = ""
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState(decisionContext = initialContext))
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        // Send a friendly initial greeting from Jelly!
        _uiState.update { 
            it.copy(
                messages = listOf(
                    ChatMessage(
                        id = UUID.randomUUID().toString(),
                        text = "Bark! Need to talk through your options? I'm here to help you chew on it! 🐶",
                        isFromJelly = true
                    )
                )
            )
        }
    }

    fun setContext(decisionContext: String) {
        _uiState.update { it.copy(decisionContext = decisionContext) }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        val userMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            text = text,
            isFromJelly = false
        )

        // Add user message to UI immediately
        _uiState.update { state ->
            state.copy(
                messages = state.messages + userMessage,
                isLoading = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            val historyToRemember = _uiState.value.messages.dropLast(1) // exclude the one we just added from "history" since the repo might take it differently, actually let's pass all previous messages.
            
            val result = aiChatRepository.sendMessage(
                context = _uiState.value.decisionContext,
                history = historyToRemember,
                message = text
            )

            when (result) {
                is Resource.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            messages = state.messages + result.data,
                            isLoading = false
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = result.exception.message ?: "Oops, I dropped the ball!"
                        )
                    }
                }
            }
        }
    }
}
