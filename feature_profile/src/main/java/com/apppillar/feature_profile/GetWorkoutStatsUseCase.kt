package com.apppillar.feature_profile

import com.apppillar.core.database.dao.WorkoutStartDao
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class GetWorkoutStatsUseCase @Inject constructor(
    private val dao: WorkoutStartDao
) {
    suspend operator fun invoke(): List<Int> {
        val now = LocalDate.now()
        val start = now.minusDays(6)
        val startMillis = start.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val map = dao.getWorkoutCountPerDay(startMillis).associateBy { it.date }

        return (0..6).map { offset ->
            val day = start.plusDays(offset.toLong())
            val key = day.toString()
            map[key]?.count ?: 0
        }
    }
}
