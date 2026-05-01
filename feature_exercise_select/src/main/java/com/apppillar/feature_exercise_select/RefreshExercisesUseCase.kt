package com.apppillar.feature_exercise_select

import javax.inject.Inject

class RefreshExercisesUseCase @Inject constructor(
    private val repo: ExerciseSelectRepositoryImpl
) {
    suspend operator fun invoke() = repo.refreshExercises()
}