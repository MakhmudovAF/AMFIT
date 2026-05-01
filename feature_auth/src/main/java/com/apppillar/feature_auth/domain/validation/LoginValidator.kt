package com.apppillar.feature_auth.domain.validation

import android.util.Patterns
import com.apppillar.feature_auth.domain.model.LoginValidationResult

object LoginValidator {
    fun validate(email: String, password: String): LoginValidationResult {
        val emailError = when {
            email.isBlank() -> "Введите email"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Некорректный email"
            else -> null
        }

        val passwordError = when {
            password.isBlank() -> "Введите пароль"
            password.length < 6 -> "Минимум 6 символов"
            else -> null
        }

        return LoginValidationResult(emailError, passwordError)
    }
}