package com.apppillar.feature_workout_create.domain.repository

import com.apppillar.feature_workout_create.domain.model.Workout

interface WorkoutCreateRepository {
    suspend fun saveWorkout(workout: Workout)
}