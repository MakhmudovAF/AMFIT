package com.apppillar.feature_home.data.repository

import com.apppillar.feature_home.data.local.dao.DailyStepsDao
import com.apppillar.feature_home.domain.repository.DailyStepsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class DailyStepsRepositoryImpl @Inject constructor(
    private val dailyStepsDao: DailyStepsDao
) : DailyStepsRepository {
    override fun getDailyStepsForToday(): Flow<Int> {
        val today = LocalDate.now().toString()
        return dailyStepsDao.getStepsForDate(today).map { it?.steps ?: 0 }
    }

    override fun getStepsForDate(date: String): Flow<Int> {
        return dailyStepsDao.getStepsForDate(date).map { it?.steps ?: 0 }
    }
}