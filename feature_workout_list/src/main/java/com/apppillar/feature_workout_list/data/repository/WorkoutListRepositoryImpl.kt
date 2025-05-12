package com.apppillar.feature_workout_list.data.repository

import com.apppillar.core.database.dao.WorkoutListDao
import com.apppillar.feature_workout_list.data.mapper.WorkoutListMapper
import com.apppillar.feature_workout_list.domain.model.Workout
import com.apppillar.feature_workout_list.domain.repository.WorkoutListRepository
import javax.inject.Inject

class WorkoutListRepositoryImpl @Inject constructor(
    private val dao: WorkoutListDao
) : WorkoutListRepository {
    override suspend fun getWorkoutList(): List<Workout> {
        return dao.getWorkoutList().map { WorkoutListMapper.fromEntity(it) }
    }

    override suspend fun deleteWorkoutById(workoutId: Long) {
        dao.deleteWorkoutById(workoutId)
    }
}