package com.apppillar.feature_profile

import com.apppillar.core.database.dao.WorkoutStartDao
import javax.inject.Inject

class CountCompletedWorkoutsUseCase @Inject constructor(
    private val dao: WorkoutStartDao
) {
    suspend operator fun invoke(): Int {
        return dao.countCompletedWorkouts()
    }
}