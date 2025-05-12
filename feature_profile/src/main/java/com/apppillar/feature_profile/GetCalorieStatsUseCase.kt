package com.apppillar.feature_profile

import android.util.Log
import com.apppillar.core.database.dao.NutritionDao
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class GetCalorieStatsUseCase @Inject constructor(
    private val dao: NutritionDao
) {
    suspend operator fun invoke(): List<Int> {
        val now = LocalDate.now()
        val start = now.minusDays(6)
        val startMillis = start.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val map = dao.getCaloriesPerDay(startMillis).associateBy { it.date }

        Log.e("Calories", "Stats from DB: $map")

        return (0..6).map { offset ->
            val day = start.plusDays(offset.toLong())
            val key = day.toString()
            Log.e("Calories", "Mapped [$offset] = $key → ${map[key]?.totalCalories}")
            map[key]?.totalCalories ?: 0
        }
    }
}