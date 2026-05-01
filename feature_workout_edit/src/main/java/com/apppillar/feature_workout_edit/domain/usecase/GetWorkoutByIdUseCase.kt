package com.apppillar.feature_workout_edit.domain.usecase

import com.apppillar.feature_workout_edit.domain.model.Workout
import com.apppillar.feature_workout_edit.domain.repository.WorkoutEditRepository
import javax.inject.Inject

class GetWorkoutByIdUseCase @Inject constructor(
    private val repository: WorkoutEditRepository
) {
    suspend operator fun invoke(id: Long): Workout {
        return repository.getWorkoutById(id)
    }
}