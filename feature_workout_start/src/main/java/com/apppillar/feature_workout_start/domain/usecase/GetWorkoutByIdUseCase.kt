package com.apppillar.feature_workout_start.domain.usecase

import com.apppillar.feature_workout_start.domain.model.Workout
import com.apppillar.feature_workout_start.domain.repository.WorkoutStartRepository
import javax.inject.Inject

class GetWorkoutByIdUseCase @Inject constructor(
    private val repository: WorkoutStartRepository
) {
    suspend operator fun invoke(id: Long): Workout {
        return repository.getWorkoutById(id)
    }
}