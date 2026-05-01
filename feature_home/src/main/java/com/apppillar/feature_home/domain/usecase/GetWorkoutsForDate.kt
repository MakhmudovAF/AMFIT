package com.apppillar.feature_home.domain.usecase

import com.apppillar.feature_home.domain.model.CompletedWorkout
import com.apppillar.feature_home.domain.repository.CompletedWorkoutsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWorkoutsForDate @Inject constructor(
    private val repository: CompletedWorkoutsRepository
) {
    operator fun invoke(date: String): Flow<List<CompletedWorkout>> = repository.getWorkoutsForDate(date)
}