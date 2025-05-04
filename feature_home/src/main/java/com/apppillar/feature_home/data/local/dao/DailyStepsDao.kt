package com.apppillar.feature_home.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apppillar.feature_home.data.local.model.DailyStepsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyStepsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dailyStepsEntity: DailyStepsEntity)

    @Query("SELECT * FROM daily_steps WHERE date = :date LIMIT 1")
    fun getStepsForDate(date: String): Flow<DailyStepsEntity?>
}