package com.apppillar.feature_home.domain.usecase

import com.apppillar.feature_home.domain.repository.CompletedWorkoutsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeeklyCompletedWorkoutsUseCase @Inject constructor(
    private val repository: CompletedWorkoutsRepository
) {
    operator fun invoke(): Flow<Int> = repository.getWeeklyWorkoutCount()
}