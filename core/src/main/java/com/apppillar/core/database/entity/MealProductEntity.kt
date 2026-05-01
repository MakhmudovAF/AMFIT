package com.apppillar.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "meal_products",
    foreignKeys = [ForeignKey(
        entity = MealEntity::class,
        parentColumns = ["id"],
        childColumns = ["mealId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("mealId")]
)
data class MealProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mealId: Long,
    val name: String,
    val grams: Int,
    val calories: Int,
    val protein: Float,
    val fat: Float,
    val carbs: Float
)