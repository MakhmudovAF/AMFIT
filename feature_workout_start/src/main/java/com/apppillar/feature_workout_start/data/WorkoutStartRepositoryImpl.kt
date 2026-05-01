package com.apppillar.feature_workout_start.data

import com.apppillar.core.database.dao.WorkoutCreateEditDao
import com.apppillar.core.database.dao.WorkoutStartDao
import com.apppillar.core.database.entity.CompletedExerciseEntity
import com.apppillar.core.database.entity.CompletedSetEntity
import com.apppillar.core.database.entity.CompletedWorkoutEntity
import com.apppillar.feature_workout_start.data.mapper.WorkoutEditMapper
import com.apppillar.feature_workout_start.domain.model.Workout
import com.apppillar.feature_workout_start.domain.repository.WorkoutStartRepository
import javax.inject.Inject

class WorkoutStartRepositoryImpl @Inject constructor(
    private val dao: WorkoutCreateEditDao,
    private val completedDao: WorkoutStartDao
) : WorkoutStartRepository {
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

    override suspend fun insertCompletedWorkout(workout: Workout, duration: Int, totalVolume: Float, totalSets: Int) {

        val workoutId = completedDao.insertCompletedWorkout(
            CompletedWorkoutEntity(
                originalWorkoutId = workout.id,
                title = workout.title,
                duration = duration,
                timestamp = System.currentTimeMillis(),
                totalVolume = totalVolume,
                totalCompletedSets = totalSets
            )
        )

        val exercises = workout.exercises.map {
            CompletedExerciseEntity(
                completedWorkoutId = workoutId,
                name = it.name,
                bodyPart = it.bodyPart,
                imageUrl = it.imageUrl,
                restDurationSeconds = it.restDurationSeconds
            )
        }

        val exerciseIds = completedDao.insertCompletedExercises(exercises)

        val sets = workout.exercises.flatMapIndexed { index, ex ->
            ex.sets.map {
                CompletedSetEntity(
                    exerciseId = exerciseIds[index],
                    weight = it.weight,
                    reps = it.reps,
                    isCompleted = it.isChecked
                )
            }
        }

        completedDao.insertCompletedSets(sets)
    }

}