package com.apppillar.feature_auth.data.repository

import com.apppillar.feature_auth.data.remote.AuthApi
import com.apppillar.feature_auth.data.remote.dto.ForgotPasswordRequestDto
import com.apppillar.feature_auth.data.remote.dto.LoginRequestDto
import com.apppillar.feature_auth.data.remote.dto.LoginResponseDto
import com.apppillar.feature_auth.data.remote.dto.RegisterRequestDto
import com.apppillar.feature_auth.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val api: AuthApi
) : AuthRepository {

    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.register(RegisterRequestDto(username, email, password))
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else Result.failure(Exception(handleError(response.code())))
            } catch (e: Exception) {
                Result.failure(Exception("Нет подключения к серверу"))
            }
        }
    }

    override suspend fun login(email: String, password: String): Result<LoginResponseDto> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.login(LoginRequestDto(email, password))
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) Result.success(data)
                    else Result.failure(Exception("Пустой токен"))
                } else {
                    Result.failure(Exception(handleError(response.code())))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Нет подключения к серверу"))
            }
        }
    }

    override suspend fun requestPasswordReset(email: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.forgotPassword(ForgotPasswordRequestDto(email))
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else Result.failure(Exception(handleError(response.code())))
            } catch (e: Exception) {
                Result.failure(Exception("Нет подключения к серверу"))
            }
        }
    }

    private fun handleError(code: Int): String = when (code) {
        400 -> "Неверные данные"
        401 -> "Неверный email или пароль"
        404 -> "Пользователь не найден"
        409 -> "Email уже зарегистрирован"
        500 -> "Ошибка сервера"
        else -> "Ошибка: $code"
    }
}