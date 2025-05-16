package com.apppillar.feature_home.util

import android.content.Context
import com.apppillar.feature_home.R

enum class GoalType(private val hintProvider: (Context) -> String,
                    private val titleProvider: (Context) -> String) {
    DAILY_STEPS(
        { context -> context.getString(R.string.step_goal) },
        { context -> context.getString(R.string.step_change_goal) }
    ),
    COMPLETED_WORKOUTS(
        { context -> context.getString(R.string.workouts_goal) },
        { context -> context.getString(R.string.workout_change_goal) }
    );

    fun getHint(context: Context) = hintProvider(context)
    fun getTitle(context: Context) = titleProvider(context)
}