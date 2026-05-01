package com.apppillar.feature_home.domain.model

data class HomeState(
    val user: String = "",
    val dailySteps: Int = 0,
    val stepsGoal: Int = 10000,
    val workouts: Int = 0,
    val workoutsGoal: Int = 5,
    val completedWorkouts: List<CompletedWorkout> = emptyList(),
    val goalAchievedMessage: String? = null
)