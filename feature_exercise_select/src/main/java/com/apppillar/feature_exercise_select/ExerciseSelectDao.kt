package com.apppillar.feature_exercise_select

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseSelectDao {

    @Query("SELECT * FROM exercises")
    fun getAll(): Flow<List<ExerciseSelectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exercises: List<ExerciseSelectEntity>)

    @Query("DELETE FROM exercises")
    suspend fun clear()
}