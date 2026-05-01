package com.apppillar.feature_home.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.apppillar.feature_home.data.local.dao.DailyStepsDao
import com.apppillar.feature_home.data.local.entity.DailyStepsEntity

@Database(
    entities = [DailyStepsEntity::class/*, CompletedWorkoutEntity::class*/],
    version = 1,
    exportSchema = false
)
abstract class HomeDatabase : RoomDatabase() {
    abstract fun stepDao(): DailyStepsDao
    //abstract fun trainingDao(): CompletedWorkoutsDao
}