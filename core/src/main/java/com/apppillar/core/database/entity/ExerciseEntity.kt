package com.apppillar.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_list")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val workoutId: Long = 0, // Внешний ключ
    val name: String = "",
    val bodyPart: String = "",
    val equipment: String = "",
    val imageUrl: String? = null,
    val restDurationSeconds: Int = 0
)