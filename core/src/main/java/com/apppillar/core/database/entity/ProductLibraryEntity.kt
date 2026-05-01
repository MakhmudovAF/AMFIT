package com.apppillar.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products_library")
data class ProductLibraryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val caloriesPer100g: Int,
    val proteinPer100g: Float,
    val fatPer100g: Float,
    val carbsPer100g: Float,
    val category: String? = null
)