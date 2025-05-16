package com.apppillar.feature_home.data.repository

import com.apppillar.core.database.dao.CompletedWorkoutListDao
import com.apppillar.feature_home.domain.model.CompletedWorkout
import com.apppillar.feature_home.domain.repository.CompletedWorkoutsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class CompletedWorkoutsRepositoryImpl @Inject constructor(
    private val dao: CompletedWorkoutListDao
) : CompletedWorkoutsRepository {
    override fun getCompletedWorkouts(): Flow<List<CompletedWorkout>> {
        return dao.getAll().map { list ->
            list.map {
                CompletedWorkout(
                    id = it.id.toString(),
                    title = it.title,
                    duration = formatDurationSmart(it.duration),
                    date = formatDate(it.timestamp)
                )
            }
        }
    }

    override fun getWeeklyWorkoutCount(): Flow<Int> {
        val monday = LocalDate.now().with(DayOfWeek.MONDAY)

        return dao.getAll().map { list ->
            list.count {
                try {
                    val date = LocalDate.parse(formatDate(it.timestamp))
                    !date.isBefore(monday) // >= monday
                } catch (e: Exception) {
                    false
                }
            }
        }
    }

    override fun getWorkoutsForDate(date: String): Flow<List<CompletedWorkout>> {
        return dao.getByDate(date).map { list ->
            list.map {
                CompletedWorkout(
                    id = it.id.toString(),
                    title = it.title,
                    duration = formatDurationSmart(it.duration),
                    date = formatDate(it.timestamp)
                )
            }
        }
    }

    /*override suspend fun addCompletedWorkout(completedWorkout: CompletedWorkout) {
        val entity = CompletedWorkoutEntity(
            title = completedWorkout.title,
            date = completedWorkout.date,
            durationMinutes = completedWorkout.duration
        )
        dao.insert(entity)
    }*/

    fun formatDurationSmart(seconds: Int): String {
        val min = seconds / 60
        val sec = seconds % 60

        return buildString {
            if (min > 0) append("$min min.")
            if (min > 0 && sec > 0) append(" ")
            if (sec > 0 || min == 0) append("$sec sec.")
        }
    }

    fun formatDate(timestamp: Long): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date(timestamp))
    }
}