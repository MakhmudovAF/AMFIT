package com.apppillar.feature_exercise_select

import com.apppillar.core.model.ExerciseSelect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExerciseSelectRepositoryImpl(
    private val api: ExerciseApi,
    private val dao: ExerciseSelectDao
) {

    fun getExercises(): Flow<List<ExerciseSelect>> =
        dao.getAll().map { it.map { entity -> entity.toModel() } }

    suspend fun refreshExercises() {
        val fromApi = api.getExercises().map { it.toEntity() }
        dao.clear()
        dao.insertAll(fromApi)
    }
}