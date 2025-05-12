package com.apppillar.feature_profile

import com.apppillar.core.database.dao.WorkoutStartDao
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class GetVolumeStatsUseCase @Inject constructor(
    private val dao: WorkoutStartDao
) {
    suspend operator fun invoke(): List<Float> {
        val now = LocalDate.now()
        val start = now.minusDays(6)
        val startMillis = start.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val map = dao.getVolumeStats(startMillis).associateBy { it.date }

        return (0..6).map { offset ->
            val day = start.plusDays(offset.toLong()).toString()
            map[day]?.totalVolume ?: 0f
        }
    }
}