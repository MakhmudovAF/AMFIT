package com.apppillar.feature_workout_start

fun estimateCalories(
    durationMinutes: Float,
    weightKg: Double,
    loadFactor: Float
): Int {
    val met = loadFactor
    return ((met * 3.5 * weightKg) / 200 * durationMinutes).toInt()
}