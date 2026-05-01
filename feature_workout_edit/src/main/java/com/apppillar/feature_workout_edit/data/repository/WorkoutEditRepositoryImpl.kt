package com.apppillar.feature_workout_edit.data.repository

import com.apppillar.core.database.dao.WorkoutCreateEditDao
import com.apppillar.feature_workout_edit.data.mapper.WorkoutEditMapper
import com.apppillar.feature_workout_edit.domain.model.Workout
import com.apppillar.feature_workout_edit.domain.repository.WorkoutEditRepository
import javax.inject.Inject

class WorkoutEditRepositoryImpl @Inject constructor(
    private val dao: WorkoutCreateEditDao
) : WorkoutEditRepository {
    override suspend fun saveWorkout(workout: Workout) {
        if (workout.id != 0L) {
            dao.deleteSetsByWorkoutId(workout.id)
            dao.deleteExercisesByWorkoutId(workout.id)
            dao.deleteWorkoutById(workout.id)
        }

        val workoutId = dao.insertWorkout(WorkoutEditMapper.toEntity(workout))

        workout.exercises.forEach { ex ->
            val exerciseEntity = WorkoutEditMapper.toEntity(ex, workoutId)
            val exerciseId = dao.insertExercise(exerciseEntity)

            val setEntities = ex.sets.map { set ->
                WorkoutEditMapper.toEntity(set, exerciseId)
            }
            dao.insertSets(setEntities)
        }
    }

    override suspend fun getWorkoutById(id: Long): Workout {
        val entity = dao.getWorkoutWithExercisesById(id)
        return WorkoutEditMapper.fromEntity(entity)
    }
}