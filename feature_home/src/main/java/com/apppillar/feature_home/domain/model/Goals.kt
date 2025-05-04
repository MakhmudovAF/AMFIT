package com.apppillar.feature_home.domain.model

import kotlinx.coroutines.flow.Flow

data class Goals(
    val dailyStepsGoal: Flow<Int>,
    val completedWorkoutsGoal: Flow<Int>
)