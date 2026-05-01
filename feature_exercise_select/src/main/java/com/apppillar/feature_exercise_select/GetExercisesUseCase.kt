package com.apppillar.feature_exercise_select

import javax.inject.Inject

class GetExercisesUseCase @Inject constructor(
    private val repo: ExerciseSelectRepositoryImpl
) {
    operator fun invoke() = repo.getExercises()
}