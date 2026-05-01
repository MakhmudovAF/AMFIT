package com.apppillar.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "completed_workout_list")
data class CompletedWorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val originalWorkoutId: Long = 0,
    val title: String = "",
    val duration: Int,
    val timestamp: Long = 0,
    val totalVolume: Float = 0F,
    val totalCompletedSets: Int = 0
)