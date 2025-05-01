package com.apppillar.feature_auth.presentation.login.state

import com.apppillar.feature_auth.domain.model.LoginValidationResult

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
    data class Message(val content: String) : LoginUiState()
    data class ValidationFailed(val validation: LoginValidationResult) : LoginUiState()
}