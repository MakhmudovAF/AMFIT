package com.apppillar.feature_auth.domain.repository

import com.apppillar.feature_auth.data.remote.dto.LoginResponseDto

interface AuthRepository {
    suspend fun register(username: String, email: String, password: String): Result<Unit>
    suspend fun login(email: String, password: String): Result<LoginResponseDto> // JWT-токен и имя пользователя
    suspend fun requestPasswordReset(email: String): Result<Unit>
}