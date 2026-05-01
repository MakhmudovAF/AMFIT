package com.apppillar.feature_nutrition.presentation.product_search

import com.apppillar.core.database.entity.MealType
import com.apppillar.core.database.entity.relation.MealWithProducts
import com.apppillar.core.model.DailySummary

data class NutritionUiState(
    val meals: Map<MealType, MealWithProducts> = emptyMap(),
    val summary: DailySummary = DailySummary(),
    val selectedDate: Long = System.currentTimeMillis()
)