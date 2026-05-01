package com.apppillar.feature_home.domain.repository

import kotlinx.coroutines.flow.Flow

interface DailyStepsRepository {
    fun getDailyStepsForToday(): Flow<Int>
    fun getStepsForDate(date: String): Flow<Int>
}