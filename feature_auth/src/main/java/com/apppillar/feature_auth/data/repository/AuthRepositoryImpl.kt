package com.apppillar.feature_auth.data.repository

import com.apppillar.feature_auth.data.remote.AuthApi
import com.apppillar.feature_auth.data.remote.dto.ForgotPasswordRequestDto
import com.apppillar.feature_auth.data.remote.dto.LoginRequestDto
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
                } else {
                    val message = when (response.code()) {
                        400 -> "Неверные данные. Проверьте поля"
                        409 -> "Email уже зарегистрирован"
                        500 -> "Ошибка сервера, попробуйте позже"
                        else -> "Ошибка: ${response.code()}"
                    }
                    Result.failure(Exception(message))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Нет подключения к серверу"))
            }
        }
    }

    override suspend fun login(email: String, password: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.login(LoginRequestDto(email, password))
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    if (!token.isNullOrBlank()) {
                        Result.success(token)
                    } else {
                        Result.failure(Exception("Пустой токен"))
                    }
                } else {
                    val message = when (response.code()) {
                        400 -> "Некорректный запрос"
                        401 -> "Неверный email или пароль"
                        403 -> "Нет доступа"
                        404 -> "Пользователь не найден"
                        409 -> "Пользователь с таким email уже зарегистрирован"
                        500 -> "Ошибка сервера"
                        else -> "Ошибка: ${response.code()}"
                    }
                    Result.failure(Exception(message))
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
                } else {
                    val message = when (response.code()) {
                        400 -> "Некорректный email"
                        404 -> "Пользователь с таким email не найден"
                        500 -> "Ошибка сервера"
                        else -> "Ошибка: ${response.code()}"
                    }
                    Result.failure(Exception(message))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Нет подключения к серверу"))
            }
        }
    }
}