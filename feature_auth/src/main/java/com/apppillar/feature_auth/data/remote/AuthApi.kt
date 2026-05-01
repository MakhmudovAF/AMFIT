package com.apppillar.feature_auth.data.remote

import com.apppillar.feature_auth.data.remote.dto.ForgotPasswordRequestDto
import com.apppillar.feature_auth.data.remote.dto.LoginRequestDto
import com.apppillar.feature_auth.data.remote.dto.LoginResponseDto
import com.apppillar.feature_auth.data.remote.dto.RegisterRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/register")
    suspend fun register(
        @Body request: RegisterRequestDto
    ): Response<Unit> // или Response<String> если API возвращает сообщение

    @POST("/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): Response<LoginResponseDto>

    @POST("/forgot-password")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequestDto
    ): Response<Unit>
}