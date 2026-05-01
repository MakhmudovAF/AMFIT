package com.apppillar.feature_home.domain.repository

import com.apppillar.feature_home.domain.model.CompletedWorkout
import kotlinx.coroutines.flow.Flow

interface CompletedWorkoutsRepository {
    fun getCompletedWorkouts(): Flow<List<CompletedWorkout>>
    fun getWeeklyWorkoutCount(): Flow<Int>
    //suspend fun addCompletedWorkout(completedWorkout: CompletedWorkout)
    fun getWorkoutsForDate(date: String): Flow<List<CompletedWorkout>>
}