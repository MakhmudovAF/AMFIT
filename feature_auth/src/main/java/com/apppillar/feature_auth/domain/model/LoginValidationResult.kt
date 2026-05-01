package com.apppillar.feature_auth.domain.model

data class LoginValidationResult(
    val emailError: String? = null,
    val passwordError: String? = null
) {
    val hasError: Boolean
        get() = emailError != null || passwordError != null
}