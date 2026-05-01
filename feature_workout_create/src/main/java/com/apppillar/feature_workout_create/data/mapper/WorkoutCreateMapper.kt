package com.apppillar.feature_workout_create.data.mapper

import com.apppillar.core.database.entity.ExerciseEntity
import com.apppillar.core.database.entity.SetEntity
import com.apppillar.core.database.entity.WorkoutEntity
import com.apppillar.core.model.ExerciseSelect
import com.apppillar.feature_workout_create.domain.model.Exercise
import com.apppillar.feature_workout_create.domain.model.Set
import com.apppillar.feature_workout_create.domain.model.Workout

object WorkoutCreateMapper {
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