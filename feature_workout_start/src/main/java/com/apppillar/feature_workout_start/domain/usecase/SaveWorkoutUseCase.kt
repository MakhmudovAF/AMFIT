package com.apppillar.feature_workout_start.domain.usecase

import com.apppillar.feature_workout_start.domain.model.Workout
import com.apppillar.feature_workout_start.domain.repository.WorkoutStartRepository
import javax.inject.Inject

class SaveWorkoutUseCase @Inject constructor(
    private val repository: WorkoutStartRepository
) {
    suspend fun invoke(workout: Workout) {
        repository.saveWorkout(workout)
    }
}