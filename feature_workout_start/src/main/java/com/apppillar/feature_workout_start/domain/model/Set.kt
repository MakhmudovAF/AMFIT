package com.apppillar.feature_workout_start.domain.model

data class Set(
    val id: Long = 0,
    var weight: String = "",
    var reps: String = "",
    var isChecked: Boolean = false
)