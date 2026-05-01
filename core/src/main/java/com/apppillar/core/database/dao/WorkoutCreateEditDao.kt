package com.apppillar.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.apppillar.core.database.entity.ExerciseEntity
import com.apppillar.core.database.entity.SetEntity
import com.apppillar.core.database.entity.WorkoutEntity
import com.apppillar.core.database.entity.relation.WorkoutWithExercises

@Dao
interface WorkoutCreateEditDao {
    @Insert
    suspend fun insertWorkout(workout: WorkoutEntity): Long

    @Insert
    suspend fun insertExercise(exercise: ExerciseEntity): Long

    @Insert
    suspend fun insertSets(sets: List<SetEntity>)

    @Transaction
    @Query("SELECT * FROM workout_list WHERE id = :id")
    suspend fun getWorkoutWithExercisesById(id: Long): WorkoutWithExercises

    @Query("DELETE FROM workout_list WHERE id = :id")
    suspend fun deleteWorkoutById(id: Long)

    @Query("DELETE FROM exercise_list WHERE workoutId = :workoutId")
    suspend fun deleteExercisesByWorkoutId(workoutId: Long)

    @Query("DELETE FROM set_list WHERE exerciseId IN (SELECT id FROM exercise_list WHERE workoutId = :workoutId)")
    suspend fun deleteSetsByWorkoutId(workoutId: Long)
}