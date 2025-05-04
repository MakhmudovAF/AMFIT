package com.apppillar.feature_home.domain.repository

import kotlinx.coroutines.flow.Flow

interface GoalsRepository {
    fun getDailyStepsGoal(): Flow<Int>
    fun getCompletedWorkoutsGoal(): Flow<Int>
    suspend fun saveDailyStepsGoal(value: Int)
    suspend fun saveCompletedWorkoutsGoal(value: Int)
}