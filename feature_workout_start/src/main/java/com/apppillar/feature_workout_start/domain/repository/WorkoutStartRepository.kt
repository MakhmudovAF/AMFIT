package com.apppillar.feature_workout_start.domain.repository

import com.apppillar.feature_workout_start.domain.model.Workout

interface WorkoutStartRepository {
    suspend fun saveWorkout(workout: Workout)
    suspend fun getWorkoutById(id: Long): Workout
    suspend fun insertCompletedWorkout(workout: Workout, duration: Int, totalVolume: Float, totalSets: Int)
}