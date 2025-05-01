package com.apppillar.feature_auth.domain.usecase

import com.apppillar.feature_auth.domain.repository.AuthRepository

class RegisterUserUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, email: String, password: String): Result<Unit> {
        return repository.register(username, email, password)
    }
}