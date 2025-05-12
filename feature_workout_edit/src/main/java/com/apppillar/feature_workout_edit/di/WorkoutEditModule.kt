package com.apppillar.feature_workout_edit.di

import com.apppillar.core.database.dao.WorkoutCreateEditDao
import com.apppillar.feature_workout_edit.data.repository.WorkoutEditRepositoryImpl
import com.apppillar.feature_workout_edit.domain.repository.WorkoutEditRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkoutEditModule {

    @Provides
    @Singleton
    fun provideWorkoutEditRepository(dao: WorkoutCreateEditDao): WorkoutEditRepository {
        return WorkoutEditRepositoryImpl(dao)
    }
}