package com.apppillar.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.apppillar.core.database.dao.CompletedWorkoutListDao
import com.apppillar.core.database.dao.WorkoutCreateEditDao
import com.apppillar.core.database.dao.WorkoutListDao
import com.apppillar.core.database.dao.WorkoutStartDao
import com.apppillar.core.database.entity.CompletedExerciseEntity
import com.apppillar.core.database.entity.CompletedSetEntity
import com.apppillar.core.database.entity.CompletedWorkoutEntity
import com.apppillar.core.database.entity.ExerciseEntity
import com.apppillar.core.database.entity.SetEntity
import com.apppillar.core.database.entity.WorkoutEntity

@Database(
    entities = [
        WorkoutEntity::class,
        ExerciseEntity::class,
        SetEntity::class,
        CompletedWorkoutEntity::class,
        CompletedExerciseEntity::class,
        CompletedSetEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WorkoutDatabase : RoomDatabase() {

    abstract fun workoutListDao(): WorkoutListDao
    abstract fun workoutCreateEditDao(): WorkoutCreateEditDao
    abstract fun workoutStartDao(): WorkoutStartDao
    abstract fun completedWorkoutListDao(): CompletedWorkoutListDao

    companion object {
        const val DATABASE_NAME = "workout_db"
    }
}