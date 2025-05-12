package com.apppillar.feature_workout_list.data.mapper

import com.apppillar.core.database.entity.relation.WorkoutWithExercises
import com.apppillar.feature_workout_list.domain.model.Exercise
import com.apppillar.feature_workout_list.domain.model.Set
import com.apppillar.feature_workout_list.domain.model.Workout

object WorkoutListMapper {
    fun fromEntity(workoutWithExercises: WorkoutWithExercises): Workout {
        return Workout(
            id = workoutWithExercises.workoutEntity.id,
            title = workoutWithExercises.workoutEntity.title,
            exercises = workoutWithExercises.exercises.map { exerciseWithSets ->
                Exercise(
                    id = exerciseWithSets.exerciseEntity.id,
                    name = exerciseWithSets.exerciseEntity.name,
                    bodyPart = exerciseWithSets.exerciseEntity.bodyPart,
                    imageUrl = exerciseWithSets.exerciseEntity.imageUrl,
                    restDurationSeconds = exerciseWithSets.exerciseEntity.restDurationSeconds,
                    sets = exerciseWithSets.sets.map { setEntity ->
                        Set(
                            id = setEntity.id,
                            weight = setEntity.weight,
                            reps = setEntity.reps
                        )
                    }
                )
            }
        )
    }
}