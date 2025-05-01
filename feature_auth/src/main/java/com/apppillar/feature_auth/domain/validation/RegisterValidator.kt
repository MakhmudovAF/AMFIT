package com.apppillar.feature_auth.domain.validation

import android.util.Patterns
import com.apppillar.feature_auth.domain.model.RegisterValidationResult

object RegisterValidator {
    fun validate(name: String, email: String, password: String): RegisterValidationResult {
        return RegisterValidationResult(
            nameError = if (name.isBlank()) "Введите имя" else null,
            emailError = when {
                email.isBlank() -> "Введите email"
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Некорректный email"
                else -> null
            },
            passwordError = if (password.length < 6) "Минимум 6 символов" else null
        )
    }
}