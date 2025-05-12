package com.apppillar.core.database

import android.content.Context
import androidx.room.Room
import com.apppillar.core.database.dao.CompletedWorkoutListDao
import com.apppillar.core.database.dao.NutritionDao
import com.apppillar.core.database.dao.ProductLibraryDao
import com.apppillar.core.database.dao.WorkoutCreateEditDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkoutListModule {
    @Provides
    @Singleton
    fun provideWorkoutDatabase(@ApplicationContext context: Context): WorkoutDatabase {
        return Room.databaseBuilder(
            context,
            WorkoutDatabase::class.java,
            WorkoutDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNutritionDatabase(@ApplicationContext context: Context): NutritionDatabase {
        return Room.databaseBuilder(
            context,
            NutritionDatabase::class.java,
            NutritionDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideWorkoutCreateEditDao(database: WorkoutDatabase): WorkoutCreateEditDao {
        return database.workoutCreateEditDao()
    }

    @Provides
    fun provideCompletedWorkoutListDao(database: WorkoutDatabase): CompletedWorkoutListDao {
        return database.completedWorkoutListDao()
    }

    @Provides
    fun provideNutritionDao(database: NutritionDatabase): NutritionDao {
        return database.nutritionDao()
    }

    @Provides
    fun provideProductLibraryDao(database: NutritionDatabase): ProductLibraryDao {
        return database.productLibraryDao()
    }

}