package com.apppillar.feature_exercise_select

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ExerciseSelectEntity::class], version = 1)
abstract class ExerciseSelectDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseSelectDao
}