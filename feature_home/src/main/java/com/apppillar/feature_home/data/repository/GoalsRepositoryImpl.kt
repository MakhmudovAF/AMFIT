package com.apppillar.feature_home.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.apppillar.feature_home.domain.repository.GoalsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class GoalsRepositoryImpl @Inject constructor(
    @Named("goals") private val dataStore: DataStore<Preferences>
) : GoalsRepository {
    private val STEP_GOAL = intPreferencesKey("step_goal")
    private val WORKOUT_GOAL = intPreferencesKey("workout_goal")

    override fun getDailyStepsGoal(): Flow<Int> =
        dataStore.data.map { it[STEP_GOAL] ?: 10000 }

    override fun getCompletedWorkoutsGoal(): Flow<Int> = dataStore.data
        .map { it[WORKOUT_GOAL] ?: 5 }

    override suspend fun saveDailyStepsGoal(value: Int) {
        dataStore.edit { it[STEP_GOAL] = value }
    }

    override suspend fun saveCompletedWorkoutsGoal(value: Int) {
        dataStore.edit { it[WORKOUT_GOAL] = value }
    }
}