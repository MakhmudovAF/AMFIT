package com.apppillar.feature_home.domain.usecase

import com.apppillar.feature_home.domain.repository.CompletedWorkoutsRepository
import com.apppillar.feature_home.domain.model.CompletedWorkout
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCompletedWorkoutsUseCase @Inject constructor(
    private val repository: CompletedWorkoutsRepository
) {
    operator fun invoke(): Flow<List<CompletedWorkout>> = repository.getCompletedWorkouts()
}