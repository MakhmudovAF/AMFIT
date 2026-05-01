package com.apppillar.feature_auth.presentation.register.state

import com.apppillar.feature_auth.domain.model.RegisterValidationResult

sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    object Success : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
    data class Message(val content: String) : RegisterUiState()
    data class ValidationFailed(val validationError: RegisterValidationResult) : RegisterUiState()
}