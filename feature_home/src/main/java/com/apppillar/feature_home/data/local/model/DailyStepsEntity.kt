package com.apppillar.feature_home.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_steps")
data class DailyStepsEntity(
    @PrimaryKey val date: String,
    val steps: Int
)