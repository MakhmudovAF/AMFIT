package com.apppillar.feature_workout_create.domain.model

import java.util.UUID

data class Set(
    val id: Long = UUID.randomUUID().mostSignificantBits,
    var weight: String = "",
    var reps: String = ""
)