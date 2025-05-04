package com.apppillar.feature_home.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.apppillar.feature_home.data.local.dao.CompletedWorkoutsDao
import com.apppillar.feature_home.data.local.dao.DailyStepsDao
import com.apppillar.feature_home.data.local.model.CompletedWorkoutEntity
import com.apppillar.feature_home.data.local.model.DailyStepsEntity

@Database(
    entities = [DailyStepsEntity::class, CompletedWorkoutEntity::class],
    version = 1,
    exportSchema = false
)
abstract class HomeDatabase : RoomDatabase() {
    abstract fun stepDao(): DailyStepsDao
    abstract fun trainingDao(): CompletedWorkoutsDao
}