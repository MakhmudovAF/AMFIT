/*
package com.apppillar.feature_home.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apppillar.feature_home.data.local.entity.CompletedWorkoutEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletedWorkoutsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(completedWorkoutEntity: CompletedWorkoutEntity)

    @Query("SELECT * FROM completed_workouts ORDER BY date DESC")
    fun getAll(): Flow<List<CompletedWorkoutEntity>>

    @Query("SELECT * FROM completed_workouts WHERE date = :date ORDER BY date DESC")
    fun getByDate(date: String): Flow<List<CompletedWorkoutEntity>>
}*/
