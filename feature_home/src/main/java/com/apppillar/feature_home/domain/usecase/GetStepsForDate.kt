package com.apppillar.feature_home.domain.usecase

import com.apppillar.feature_home.domain.repository.DailyStepsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStepsForDate @Inject constructor(
    private val repository: DailyStepsRepository
) {
    operator fun invoke(date: String): Flow<Int> = repository.getStepsForDate(date)
}