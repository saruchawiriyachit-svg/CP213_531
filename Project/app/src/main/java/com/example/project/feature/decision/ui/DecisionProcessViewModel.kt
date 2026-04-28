package com.example.project.feature.decision.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.ai.domain.AiRecommendationRepository
import com.example.project.core.error.Resource
import com.example.project.feature.decision.domain.Decision
import com.example.project.feature.decision.domain.DecisionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DecisionProcessViewModel(
    private val decisionRepository: DecisionRepository,
    private val aiRecommendationRepository: AiRecommendationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DecisionUiState())
    val uiState: StateFlow<DecisionUiState> = _uiState.asStateFlow()

    fun updateCurrentDecision(decision: Decision) {
        _uiState.update { it.copy(currentDecision = decision) }
    }

    fun requestRecommendation() {
        val decision = _uiState.value.currentDecision ?: return
        
        _uiState.update { 
            it.copy(
                isLoading = true, 
                mascotState = MascotState.THINKING,
                errorMessage = null
            ) 
        }

        viewModelScope.launch {
            val result = aiRecommendationRepository.getRecommendation(decision)
            
            when (result) {
                is Resource.Success -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            aiRecommendation = result.data,
                            mascotState = MascotState.EXCITED
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            errorMessage = result.exception.message ?: "An error occurred",
                            mascotState = MascotState.SAD
                        )
                    }
                }
            }
        }
    }
}
