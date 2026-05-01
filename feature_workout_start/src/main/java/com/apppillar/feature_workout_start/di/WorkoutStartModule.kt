package com.apppillar.feature_workout_start.di

import com.apppillar.core.database.WorkoutDatabase
import com.apppillar.core.database.dao.WorkoutCreateEditDao
import com.apppillar.core.database.dao.WorkoutStartDao
import com.apppillar.feature_workout_start.data.WorkoutStartRepositoryImpl
import com.apppillar.feature_workout_start.domain.repository.WorkoutStartRepository
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
    fun provideWorkoutStartRepository(
        dao: WorkoutCreateEditDao,
        completedDao: WorkoutStartDao
    ): WorkoutStartRepository {
        return WorkoutStartRepositoryImpl(dao, completedDao)
    }

    @Provides
    fun provideWorkoutStartDao(database: WorkoutDatabase): WorkoutStartDao {
        return database.workoutStartDao()
    }
}