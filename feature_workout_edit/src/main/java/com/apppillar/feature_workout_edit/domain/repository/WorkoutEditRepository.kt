package com.apppillar.feature_workout_edit.domain.repository

import com.apppillar.feature_workout_edit.domain.model.Workout

interface WorkoutEditRepository {
    suspend fun saveWorkout(workout: Workout)
    suspend fun getWorkoutById(id: Long): Workout
}