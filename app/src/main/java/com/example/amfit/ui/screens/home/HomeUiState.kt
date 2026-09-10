package com.example.amfit.ui.screens.home

import com.example.amfit.data.FinishedWorkout
import com.example.amfit.data.WeekDay

data class HomeUiState(
    val userName: String = "Amir",
    val dailySteps: Int = 3691,
    val dailyStepsGoal: Int = 10_000,
    val weekStreak: Int = 12,
    val weekStreakGoal: Int = 22,
    val caloriesBurned: Int = 915,
    val caloriesGoal: Int = 1_500,
    val weekDays: List<WeekDay> = emptyList(),
    val finishedWorkouts: List<FinishedWorkout> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
