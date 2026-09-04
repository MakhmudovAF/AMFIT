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
            ),
            FinishedWorkout(
                id = 4L,
                title = "Morning Workout",
                duration = "45 min"
            ),
            FinishedWorkout(
                id = 5L,
                title = "Upper Body",
                duration = "38 min"
            ),
            FinishedWorkout(
                id = 6L,
                title = "Lower Body",
                duration = "52 min"
            ),
            FinishedWorkout(
                id = 7L,
                title = "Morning Workout",
                duration = "45 min"
            ),
            FinishedWorkout(
                id = 8L,
                title = "Upper Body",
                duration = "38 min"
            ),
            FinishedWorkout(
                id = 9L,
                title = "Lower Body",
                duration = "52 min"
            ),
            FinishedWorkout(
                id = 10L,
                title = "Morning Workout",
                duration = "45 min"
            ),
            FinishedWorkout(
                id = 11L,
                title = "Upper Body",
                duration = "38 min"
            ),
            FinishedWorkout(
                id = 12L,
                title = "Lower Body",
                duration = "52 min"
            )
        )
    }
}
