package com.apppillar.feature_auth.di

import com.apppillar.feature_auth.data.remote.AuthApi
import com.apppillar.feature_auth.data.repository.AuthRepositoryImpl
import com.apppillar.feature_auth.domain.repository.AuthRepository
import com.apppillar.feature_auth.domain.usecase.ForgotPasswordUseCase
import com.apppillar.feature_auth.domain.usecase.LoginUserUseCase
import com.apppillar.feature_auth.domain.usecase.RegisterUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.150:8080/") // Для эмулятора Android → localhost
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        api: AuthApi
    ): AuthRepository {
        return AuthRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideRegisterUserUseCase(
        repository: AuthRepository
    ): RegisterUserUseCase {
        return RegisterUserUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLoginUserUseCase(
        repository: AuthRepository
    ): LoginUserUseCase {
        return LoginUserUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideForgotPasswordUseCase(
        repository: AuthRepository
    ): ForgotPasswordUseCase {
        return ForgotPasswordUseCase(repository)
    }

    /*// Если используется DataStore в feature_auth:
    @Provides
    @Singleton
    fun provideAuthPreferences(
        context: android.content.Context
    ): AuthPreferences {
        return AuthPreferences(context)
    }*/
}