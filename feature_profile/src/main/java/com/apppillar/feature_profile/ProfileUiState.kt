package com.apppillar.feature_profile

data class ProfileUiState(
    val username: String = "",
    val completedWorkoutsCount: Int = 0,
    val volumeStats: List<Float> = emptyList(),
    val setsStats: List<Int> = emptyList(),
    val maxWeightStats: List<Float> = emptyList(),
    val calorieStats: List<Int> = emptyList(),
    val proteinStats: List<Int> = emptyList(),
    val fatStats: List<Int> = emptyList(),
    val carbsStats: List<Int> = emptyList()
)
