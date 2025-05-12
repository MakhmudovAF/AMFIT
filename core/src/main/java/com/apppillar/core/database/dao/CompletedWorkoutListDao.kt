package com.apppillar.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.apppillar.core.database.entity.CompletedWorkoutEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletedWorkoutListDao {
    @Query("SELECT * FROM completed_workout_list ORDER BY timestamp DESC")
    fun getAll(): Flow<List<CompletedWorkoutEntity>>

    @Query("SELECT * FROM completed_workout_list WHERE timestamp = :date ORDER BY timestamp DESC")
    fun getByDate(date: String): Flow<List<CompletedWorkoutEntity>>
}