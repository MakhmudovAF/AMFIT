package com.apppillar.feature_home.presentation.detail

import com.apppillar.core.database.entity.relation.CompletedWorkoutWithExercises

fun CompletedWorkoutWithExercises.toUiState(): CompletedWorkoutDetailUiState {
    return CompletedWorkoutDetailUiState(
        name = workout.title,
        durationFormatted = formatDuration(workout.duration),
        totalVolume = "${workout.totalVolume.toFloat()}",
        totalSets = "${workout.totalCompletedSets}",
        exercises = exercises.map { ex ->
            ExerciseWithSetsUi(
                name = ex.exercise.name,
                bodyPart = ex.exercise.bodyPart,
                equipment = ex.exercise.equipment,
                imageUrl = ex.exercise.imageUrl,
                sets = ex.sets.map {
                    SetUi(
                        reps = it.reps,
                        weight = it.weight,
                        isCompleted = it.isCompleted
                    )
                }
            )
        }
    )
}

fun formatDuration(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%d:%02d", m, s)
}
