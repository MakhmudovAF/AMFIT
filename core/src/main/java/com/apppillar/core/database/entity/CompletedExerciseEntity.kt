package com.apppillar.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "completed_exercise_list")
data class CompletedExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val completedWorkoutId: Long = 0,
    val name: String = "",
    val bodyPart: String = "",
    val equipment: String = "",
    val imageUrl: String? = null,
    val restDurationSeconds: Int = 0
)