package com.apppillar.core.model

data class UserProfile(
    val gender: Gender,
    val age: Int,
    val weightKg: Double,
    val heightCm: Double,
    val activityLevel: Double, // коэффициент активности: от 1.2 до 1.9
    val goal: Goal
)