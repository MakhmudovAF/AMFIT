package com.apppillar.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "completed_set_list")
data class CompletedSetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val exerciseId: Long = 0,
    val weight: String = "",
    val reps: String = "",
    val isCompleted: Boolean
)