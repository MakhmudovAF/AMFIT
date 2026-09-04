package com.example.amfit.data

class WorkoutRepository {

    fun getFinishedWorkouts(): List<FinishedWorkout> {
        return listOf(
            FinishedWorkout(
                id = 1L,
                title = "Morning Workout",
                duration = "45 min"
            ),
            FinishedWorkout(
                id = 2L,
                title = "Upper Body",
                duration = "38 min"
            ),
            FinishedWorkout(
                id = 3L,
                title = "Lower Body",
                duration = "52 min"
            )
        )
    }
}
