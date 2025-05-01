package com.apppillar.feature_auth.domain.repository

interface AuthRepository {
    suspend fun register(username: String, email: String, password: String): Result<Unit>
    suspend fun login(email: String, password: String): Result<String> // JWT-токен
    suspend fun requestPasswordReset(email: String): Result<Unit>
}