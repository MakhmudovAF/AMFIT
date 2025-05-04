package com.apppillar.feature_home.data.repository

import com.apppillar.feature_home.data.local.dao.CompletedWorkoutsDao
import com.apppillar.feature_home.data.local.model.CompletedWorkoutEntity
import com.apppillar.feature_home.domain.model.CompletedWorkout
import com.apppillar.feature_home.domain.repository.CompletedWorkoutsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CompletedWorkoutsRepositoryImpl @Inject constructor(
    private val dao: CompletedWorkoutsDao
) : CompletedWorkoutsRepository {
    override fun getCompletedWorkouts(): Flow<List<CompletedWorkout>> {
        return dao.getAll().map { list ->
            list.map {
                CompletedWorkout(
                    id = it.id.toString(),
                    title = it.title,
                    date = it.date,
                    duration = it.durationMinutes
                )
            }
        }
    }

    override fun getWeeklyWorkoutCount(): Flow<Int> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val monday = LocalDate.now().with(DayOfWeek.MONDAY)

        return dao.getAll().map { list ->
            list.count {
                try {
                    val date = LocalDate.parse(it.date, formatter)
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
                    date = it.date,
                    duration = it.durationMinutes
                )
            }
        }
    }

    override suspend fun addCompletedWorkout(completedWorkout: CompletedWorkout) {
        val entity = CompletedWorkoutEntity(
            title = completedWorkout.title,
            date = completedWorkout.date,
            durationMinutes = completedWorkout.duration
        )
        dao.insert(entity)
    }
}