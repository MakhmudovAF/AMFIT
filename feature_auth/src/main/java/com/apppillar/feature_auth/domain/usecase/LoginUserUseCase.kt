package com.apppillar.feature_auth.domain.usecase

import com.apppillar.feature_auth.domain.repository.AuthRepository

class LoginUserUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<String> {
        return repository.login(email, password)
    }
}