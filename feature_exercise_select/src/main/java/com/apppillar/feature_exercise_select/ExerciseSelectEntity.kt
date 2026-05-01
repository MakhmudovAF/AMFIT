package com.apppillar.feature_exercise_select

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class ExerciseSelectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val bodyPart: String,
    val equipment: String,
    val gifUrl: String
)