package com.apppillar.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Long,                     // timestamp дня (без времени)
    val type: MealType,                // enum: BREAKFAST, LUNCH, DINNER, SNACK
)