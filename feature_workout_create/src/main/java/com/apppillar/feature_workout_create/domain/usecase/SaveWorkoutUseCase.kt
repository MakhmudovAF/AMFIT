package com.apppillar.feature_workout_create.domain.usecase

import com.apppillar.feature_workout_create.domain.model.Workout
import com.apppillar.feature_workout_create.domain.repository.WorkoutCreateRepository
import javax.inject.Inject

class SaveWorkoutUseCase @Inject constructor(
    private val repository: WorkoutCreateRepository
) {
    suspend fun invoke(workout: Workout) {
        repository.saveWorkout(workout)
    }
}