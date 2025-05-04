package com.apppillar.core.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val DATASTORE_NAME = "amfit_prefs"

val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

class TokenDataStore @Inject constructor(
    private val context: Context
) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val STEP_GOAL = stringPreferencesKey("step_goal")
        private val WORKOUT_GOAL = stringPreferencesKey("workout_goal")
    }

    val token: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }

    val username: Flow<String?> = context.dataStore.data.map { it[USERNAME_KEY] }

    val stepGoal: Flow<String?> = context.dataStore.data.map { it[STEP_GOAL] }

    val workoutGoal: Flow<String?> = context.dataStore.data.map { it[WORKOUT_GOAL] }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    suspend fun saveUsername(username: String) {
        context.dataStore.edit { prefs ->
            prefs[USERNAME_KEY] = username
        }
    }

    suspend fun clearToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
            prefs.remove(USERNAME_KEY)
        }
    }
}