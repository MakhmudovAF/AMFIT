package com.apppillar.feature_workout_start.domain.model

data class Workout(
    val id: Long = 0,
    val title: String = "",
    val exercises: List<Exercise> = emptyList<Exercise>()
)