package com.apppillar.core.database.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.apppillar.core.database.entity.MealEntity
import com.apppillar.core.database.entity.MealProductEntity

data class MealWithProducts(
    @Embedded val meal: MealEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "mealId"
    )
    val products: List<MealProductEntity>
)