package com.apppillar.feature_exercise_select

import com.apppillar.core.model.ExerciseDto
import retrofit2.http.GET
import retrofit2.http.Headers

interface ExerciseApi {
    @Headers("X-RapidAPI-Key: 5d5b39dce6mshe7c6d2c23a4b929p11f46ejsnc849c49cfd48")
    @GET("exercises?limit=1300")
    suspend fun getExercises(): List<ExerciseDto>
}