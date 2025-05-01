package com.apppillar.feature_auth.domain.model

data class RegisterValidationResult(
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null
) {
    val hasError: Boolean
        get() = nameError != null || emailError != null || passwordError != null
}