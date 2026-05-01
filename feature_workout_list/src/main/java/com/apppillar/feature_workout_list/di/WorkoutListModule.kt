package com.apppillar.feature_workout_list.di

import com.apppillar.core.database.WorkoutDatabase
import com.apppillar.core.database.dao.WorkoutListDao
import com.apppillar.feature_workout_list.data.repository.WorkoutListRepositoryImpl
import com.apppillar.feature_workout_list.domain.repository.WorkoutListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkoutListModule {
    @Provides
    fun provideWorkoutDao(database: WorkoutDatabase): WorkoutListDao {
        return database.workoutListDao()
    }

    @Provides
    @Singleton
    fun provideWorkoutListRepository(
        dao: WorkoutListDao
    ): WorkoutListRepository {
        return WorkoutListRepositoryImpl(dao)
    }
}