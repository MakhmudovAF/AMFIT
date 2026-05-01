package com.apppillar.feature_workout_create.di

import com.apppillar.core.database.dao.WorkoutCreateEditDao
import com.apppillar.feature_workout_create.data.repository.WorkoutCreateRepositoryImpl
import com.apppillar.feature_workout_create.domain.repository.WorkoutCreateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkoutCreateModule {

    @Provides
    @Singleton
    fun provideWorkoutCreateRepository(dao: WorkoutCreateEditDao): WorkoutCreateRepository {
        return WorkoutCreateRepositoryImpl(dao)
    }
}