package com.apppillar.feature_auth.domain.usecase

import com.apppillar.feature_auth.domain.repository.AuthRepository

class ForgotPasswordUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        return repository.requestPasswordReset(email)
    }
}