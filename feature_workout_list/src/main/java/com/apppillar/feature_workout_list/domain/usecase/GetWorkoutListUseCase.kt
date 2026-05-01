package com.apppillar.feature_workout_list.domain.usecase

import com.apppillar.feature_workout_list.domain.model.Workout
import com.apppillar.feature_workout_list.domain.repository.WorkoutListRepository
import javax.inject.Inject

class GetWorkoutListUseCase @Inject constructor(private val repository: WorkoutListRepository) {
    suspend fun invoke(): List<Workout> = repository.getWorkoutList()
}