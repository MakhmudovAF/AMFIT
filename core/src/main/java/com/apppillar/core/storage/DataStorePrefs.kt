package com.apppillar.core.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.apppillar.core.model.Gender
import com.apppillar.core.model.Goal
import com.apppillar.core.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val DATASTORE_NAME = "amfit_prefs"

val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

class DataStorePrefs @Inject constructor(
    private val context: Context
) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val USERNAME_KEY = stringPreferencesKey("username")

        private val STEP_GOAL = stringPreferencesKey("step_goal")
        private val WORKOUT_GOAL = stringPreferencesKey("workout_goal")

        val KEY_GENDER = stringPreferencesKey("gender")
        val KEY_AGE = intPreferencesKey("age")
        val KEY_WEIGHT = floatPreferencesKey("weight")
        val KEY_HEIGHT = floatPreferencesKey("height")
        val KEY_ACTIVITY = floatPreferencesKey("activity")
        val KEY_GOAL = stringPreferencesKey("goal")
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

    suspend fun saveUserProfile(profile: UserProfile) {
        context.dataStore.edit { prefs ->
            prefs[KEY_GENDER] = profile.gender.name
            prefs[KEY_AGE] = profile.age
            prefs[KEY_WEIGHT] = profile.weightKg.toFloat()
            prefs[KEY_HEIGHT] = profile.heightCm.toFloat()
            prefs[KEY_ACTIVITY] = profile.activityLevel.toFloat()
            prefs[KEY_GOAL] = profile.goal.name
        }
    }

    fun getUserProfile(): Flow<UserProfile?> = context.dataStore.data.map { prefs ->
        val gender = prefs[KEY_GENDER]?.let { Gender.valueOf(it) }
        val goal = prefs[KEY_GOAL]?.let { Goal.valueOf(it) }

        if (gender != null && goal != null) {
            UserProfile(
                gender = gender,
                age = prefs[KEY_AGE] ?: return@map null,
                weightKg = prefs[KEY_WEIGHT]?.toDouble() ?: return@map null,
                heightCm = prefs[KEY_HEIGHT]?.toDouble() ?: return@map null,
                activityLevel = prefs[KEY_ACTIVITY]?.toDouble() ?: return@map null,
                goal = goal
            )
        } else null
    }

    suspend fun isProfileSet(): Boolean {
        return context.dataStore.data.first()[KEY_AGE] != null
    }

    suspend fun clearToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
            prefs.remove(USERNAME_KEY)
        }
    }
}