package com.apppillar.feature_workout_start.domain.usecase

import com.apppillar.feature_workout_start.domain.model.Workout
import com.apppillar.feature_workout_start.domain.repository.WorkoutStartRepository
import javax.inject.Inject

class AddCompletedWorkoutUseCase @Inject constructor(
    private val repository: WorkoutStartRepository
) {
    suspend operator fun invoke(workout: Workout, duration: Int, totalVolume: Float, totalSets: Int) {
        repository.insertCompletedWorkout(workout, duration, totalVolume, totalSets)
    }
}