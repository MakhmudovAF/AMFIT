package com.apppillar.feature_home.domain.usecase

import com.apppillar.feature_home.domain.repository.GoalsRepository
import com.apppillar.feature_home.util.GoalType
import javax.inject.Inject

class SaveGoalUseCase @Inject constructor(
    private val repository: GoalsRepository
) {
    suspend operator fun invoke(type: GoalType, value: Int) {
        when (type) {
            GoalType.DAILY_STEPS -> repository.saveDailyStepsGoal(value)
            GoalType.COMPLETED_WORKOUTS -> repository.saveCompletedWorkoutsGoal(value)
        }
    }
}