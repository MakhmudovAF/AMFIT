package com.apppillar.feature_home.di

import com.apppillar.feature_home.data.repository.CompletedWorkoutsRepositoryImpl
import com.apppillar.feature_home.data.repository.DailyStepsRepositoryImpl
import com.apppillar.feature_home.data.repository.GoalsRepositoryImpl
import com.apppillar.feature_home.domain.repository.CompletedWorkoutsRepository
import com.apppillar.feature_home.domain.repository.DailyStepsRepository
import com.apppillar.feature_home.domain.repository.GoalsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeBindingModule {

    @Binds
    @Singleton
    abstract fun bindDailyStepsRepository(
        impl: DailyStepsRepositoryImpl
    ): DailyStepsRepository

    @Binds
    @Singleton
    abstract fun bindGoalsRepository(
        impl: GoalsRepositoryImpl
    ): GoalsRepository

    @Binds
    @Singleton
    abstract fun bindCompletedWorkoutsRepository(
        impl: CompletedWorkoutsRepositoryImpl
    ): CompletedWorkoutsRepository
}