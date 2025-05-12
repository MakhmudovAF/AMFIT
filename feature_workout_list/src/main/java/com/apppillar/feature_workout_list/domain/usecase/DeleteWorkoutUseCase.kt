package com.apppillar.feature_workout_list.domain.usecase

import com.apppillar.feature_workout_list.domain.repository.WorkoutListRepository
import javax.inject.Inject

class DeleteWorkoutUseCase @Inject constructor(
    private val repository: WorkoutListRepository
) {
    suspend operator fun invoke(workoutId: Long) {
        repository.deleteWorkoutById(workoutId)
    }
}