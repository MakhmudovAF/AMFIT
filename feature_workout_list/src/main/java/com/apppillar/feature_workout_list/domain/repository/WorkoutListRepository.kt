package com.apppillar.feature_workout_list.domain.repository

import com.apppillar.feature_workout_list.domain.model.Workout

interface WorkoutListRepository {
    suspend fun getWorkoutList(): List<Workout>
    suspend fun deleteWorkoutById(workoutId: Long)
}