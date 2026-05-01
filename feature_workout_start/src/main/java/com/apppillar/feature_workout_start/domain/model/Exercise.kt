package com.apppillar.feature_workout_start.domain.model

data class Exercise(
    val id: Long = 0,
    val name: String = "",
    val bodyPart: String = "",
    val equipment: String = "",
    val imageUrl: String? = null,
    val restDurationSeconds: Int = 0,
    val sets: List<com.apppillar.feature_workout_start.domain.model.Set> = emptyList<com.apppillar.feature_workout_start.domain.model.Set>()
)