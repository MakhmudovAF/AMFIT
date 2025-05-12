package com.apppillar.feature_home.presentation.detail

data class CompletedWorkoutDetailUiState(
    val name: String,
    val durationFormatted: String,
    val totalVolume: String,
    val totalSets: String,
    val exercises: List<ExerciseWithSetsUi>
)

data class ExerciseWithSetsUi(
    val name: String,
    val bodyPart: String,
    val equipment: String,
    val imageUrl: String?,
    val sets: List<SetUi>
)

data class SetUi(
    val reps: String,
    val weight: String,
    val isCompleted: Boolean
)