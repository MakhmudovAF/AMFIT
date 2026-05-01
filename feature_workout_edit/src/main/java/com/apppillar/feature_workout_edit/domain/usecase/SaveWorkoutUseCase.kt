package com.apppillar.feature_workout_edit.domain.usecase

import com.apppillar.feature_workout_edit.domain.model.Workout
import com.apppillar.feature_workout_edit.domain.repository.WorkoutEditRepository
import javax.inject.Inject

class SaveWorkoutUseCase @Inject constructor(
    private val repository: WorkoutEditRepository
) {
    suspend fun invoke(workout: Workout) {
        repository.saveWorkout(workout)
    }
}