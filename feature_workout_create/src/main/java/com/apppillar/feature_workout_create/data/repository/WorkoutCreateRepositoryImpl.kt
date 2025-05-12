package com.apppillar.feature_workout_create.data.repository

import com.apppillar.core.database.dao.WorkoutCreateEditDao
import com.apppillar.feature_workout_create.data.mapper.WorkoutCreateMapper
import com.apppillar.feature_workout_create.domain.model.Workout
import com.apppillar.feature_workout_create.domain.repository.WorkoutCreateRepository
import javax.inject.Inject

class WorkoutCreateRepositoryImpl @Inject constructor(
    private val dao: WorkoutCreateEditDao
) : WorkoutCreateRepository {
    override suspend fun saveWorkout(workout: Workout) {
        if (workout.id != 0L) {
            dao.deleteSetsByWorkoutId(workout.id)
            dao.deleteExercisesByWorkoutId(workout.id)
            dao.deleteWorkoutById(workout.id)
        }

        val workoutId = dao.insertWorkout(WorkoutCreateMapper.toEntity(workout))

        workout.exercises.forEach { ex ->
            val exerciseEntity = WorkoutCreateMapper.toEntity(ex, workoutId)
            val exerciseId = dao.insertExercise(exerciseEntity)

            val setEntities = ex.sets.map { set ->
                WorkoutCreateMapper.toEntity(set, exerciseId)
            }
            dao.insertSets(setEntities)
        }
    }
}