package com.apppillar.feature_workout_start.data.mapper

import com.apppillar.core.database.entity.ExerciseEntity
import com.apppillar.core.database.entity.SetEntity
import com.apppillar.core.database.entity.WorkoutEntity
import com.apppillar.core.database.entity.relation.WorkoutWithExercises
import com.apppillar.core.model.ExerciseSelect
import com.apppillar.feature_workout_start.domain.model.Exercise
import com.apppillar.feature_workout_start.domain.model.Set
import com.apppillar.feature_workout_start.domain.model.Workout

object WorkoutEditMapper {
    fun fromEntity(entity: WorkoutWithExercises): Workout {
        return Workout(
            id = entity.workoutEntity.id,
            title = entity.workoutEntity.title,
            exercises = entity.exercises.map { ex ->
                Exercise(
                    id = ex.exerciseEntity.id,
                    name = ex.exerciseEntity.name,
                    bodyPart = ex.exerciseEntity.bodyPart,
                    imageUrl = ex.exerciseEntity.imageUrl,
                    restDurationSeconds = ex.exerciseEntity.restDurationSeconds,
                    sets = ex.sets.map { set ->
                        com.apppillar.feature_workout_start.domain.model.Set(
                            id = set.id,
                            weight = set.weight,
                            reps = set.reps
                        )
                    }
                )
            }
        )
    }

    fun toEntity(workout: Workout): WorkoutEntity {
        return WorkoutEntity(
            id = workout.id,
            title = workout.title
        )
    }

    fun toEntity(exercise: Exercise, workoutId: Long): ExerciseEntity {
        return ExerciseEntity(
            id = exercise.id,
            workoutId = workoutId,
            name = exercise.name,
            bodyPart = exercise.bodyPart,
            imageUrl = exercise.imageUrl,
            restDurationSeconds = exercise.restDurationSeconds
        )
    }

    fun toEntity(set: Set, exerciseId: Long): SetEntity {
        return SetEntity(
            id = set.id,
            exerciseId = exerciseId,
            weight = set.weight,
            reps = set.reps
        )
    }

    fun ExerciseSelect.toExercise(): Exercise {
        return Exercise(
            id = this.id.toLong(),
            name = this.name,
            bodyPart = this.bodyPart,
            equipment = this.equipment,
            imageUrl = this.imageUrl,
            restDurationSeconds = 0,      // по умолчанию
            sets = emptyList()            // пока нет подходов
        )
    }
}