package com.apppillar.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "set_list")
data class SetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val exerciseId: Long = 0, // Внешний ключ
    val weight: String = "",
    val reps: String = ""
)