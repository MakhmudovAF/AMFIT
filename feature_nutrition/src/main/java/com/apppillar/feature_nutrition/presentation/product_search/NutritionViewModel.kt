package com.apppillar.feature_nutrition.presentation.product_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apppillar.core.database.dao.NutritionDao
import com.apppillar.core.database.entity.MealEntity
import com.apppillar.core.database.entity.MealProductEntity
import com.apppillar.core.database.entity.MealType
import com.apppillar.core.model.DailySummary
import com.apppillar.core.model.Gender
import com.apppillar.core.model.Goal
import com.apppillar.core.model.NutritionTargets
import com.apppillar.core.model.ProductSelection
import com.apppillar.core.model.UserProfile
import com.apppillar.core.storage.DataStorePrefs
import com.apppillar.feature_nutrition.data.ProductPreload
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class NutritionViewModel @Inject constructor(
    private val dao: NutritionDao,
    private val preload: ProductPreload,
    private val dataStorePrefs: DataStorePrefs
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _isDateFiltered = MutableStateFlow(false)
    val isDateFiltered: StateFlow<Boolean> = _isDateFiltered.asStateFlow()

    private val _uiState = MutableStateFlow(NutritionUiState())
    val uiState: StateFlow<NutritionUiState> = _uiState.asStateFlow()

    private val _nutritionTarget = MutableStateFlow<NutritionTargets?>(null)
    val nutritionTarget: StateFlow<NutritionTargets?> = _nutritionTarget.asStateFlow()

    init {
        viewModelScope.launch {
            preload.preloadIfEmpty()
        }

        viewModelScope.launch {
            dataStorePrefs.getUserProfile().collectLatest { profile ->
                if (profile != null) {
                    _nutritionTarget.value = calculateNutrition(profile)
                }
            }
        }

        loadDataForDate(_selectedDate.value)
    }

    fun setDate(date: LocalDate) {
        _selectedDate.value = date
        _isDateFiltered.value = date != LocalDate.now()
        loadDataForDate(date)
    }

    fun resetDateFilter() {
        _selectedDate.value = LocalDate.now()
        _isDateFiltered.value = false
        loadDataForDate(LocalDate.now())
    }

    private fun loadDataForDate(date: LocalDate) {
        viewModelScope.launch {
            val millis = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val meals = dao.getMealsWithProductsByDate(millis)
            val map = meals.associateBy { it.meal.type }

            val summary = meals.flatMap { it.products }.fold(DailySummary()) { acc, item ->
                acc.copy(
                    calories = acc.calories + item.calories,
                    protein = acc.protein + item.protein,
                    fat = acc.fat + item.fat,
                    carbs = acc.carbs + item.carbs
                )
            }

            _uiState.value = NutritionUiState(
                meals = map,
                summary = summary,
                selectedDate = millis
            )
        }
    }


    fun addProductToMeal(type: MealType, product: ProductSelection) {
        viewModelScope.launch {
            //val date = _selectedDate.value
            val existingMeal = _uiState.value.meals[type]?.meal
            val millis = _selectedDate.value.atStartOfDay(ZoneId.systemDefault())
                .toInstant().toEpochMilli()
            val mealId = existingMeal?.id ?: dao.insertMeal(MealEntity(date = millis, type = type))

            dao.insertMealProduct(
                MealProductEntity(
                    mealId = mealId,
                    name = product.name,
                    grams = product.grams,
                    calories = product.calories,
                    protein = product.protein,
                    fat = product.fat,
                    carbs = product.carbs
                )
            )

            //updateUi(date)
            loadDataForDate(_selectedDate.value)
        }
    }

    fun deleteMealProduct(id: Long) {
        viewModelScope.launch {
            dao.deleteProductById(id)
            loadDataForDate(_selectedDate.value)
        }
    }

    fun calculateNutrition(user: UserProfile): NutritionTargets {
        // 1. BMR
        val bmr = when (user.gender) {
            Gender.MALE -> (10 * user.weightKg) + (6.25 * user.heightCm) - (5 * user.age) + 5
            Gender.FEMALE -> (10 * user.weightKg) + (6.25 * user.heightCm) - (5 * user.age) - 161
        }

        // 2. TDEE
        val tdee = bmr * user.activityLevel

        // 3. Учитываем цель
        val goalMultiplier = when (user.goal) {
            Goal.LOSE_WEIGHT -> 0.9
            Goal.MAINTAIN -> 1.0
            Goal.GAIN_WEIGHT -> 1.1
        }

        val calories = (tdee * goalMultiplier).toInt()

        // 4. КБЖУ из калорий
        val protein = (calories * 0.3 / 4).toInt() // 4 ккал на грамм
        val fat = (calories * 0.3 / 9).toInt()     // 9 ккал на грамм
        val carbs = (calories * 0.4 / 4).toInt()   // 4 ккал на грамм

        return NutritionTargets(
            calories = calories,
            proteinGrams = protein,
            fatGrams = fat,
            carbsGrams = carbs
        )
    }
}