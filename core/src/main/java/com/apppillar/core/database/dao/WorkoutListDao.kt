package com.apppillar.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.apppillar.core.database.entity.relation.WorkoutWithExercises

@Dao
interface WorkoutListDao {
    @Transaction
    @Query("SELECT * FROM workout_list ORDER BY id DESC")
    suspend fun getWorkoutList(): List<WorkoutWithExercises>

    @Query("DELETE FROM workout_list WHERE id = :id")
    suspend fun deleteWorkoutById(id: Long)
}