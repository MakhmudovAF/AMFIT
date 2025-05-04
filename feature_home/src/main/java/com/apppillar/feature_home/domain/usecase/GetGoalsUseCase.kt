package com.apppillar.feature_home.domain.usecase

import com.apppillar.feature_home.domain.model.Goals
import com.apppillar.feature_home.domain.repository.GoalsRepository
import javax.inject.Inject

class GetGoalsUseCase @Inject constructor(
    private val repository: GoalsRepository
) {
    operator fun invoke(): Goals {
        return Goals(
            dailyStepsGoal = repository.getDailyStepsGoal(),
            completedWorkoutsGoal = repository.getCompletedWorkoutsGoal()
        )
    }
}