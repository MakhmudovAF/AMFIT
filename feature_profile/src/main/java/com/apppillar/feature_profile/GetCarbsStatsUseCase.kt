package com.apppillar.feature_profile

import com.apppillar.core.database.dao.NutritionDao
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class GetCarbsStatsUseCase @Inject constructor(
    private val dao: NutritionDao
) {
    suspend operator fun invoke(): List<Int> {
        val start = LocalDate.now().minusDays(6)
        val startMillis = start.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val stats = dao.getMacrosPerDay(startMillis).associateBy { it.date }
        return (0..6).map { offset ->
            val date = start.plusDays(offset.toLong()).toString()
            stats[date]?.totalCarbs?.toInt() ?: 0
        }
    }
}