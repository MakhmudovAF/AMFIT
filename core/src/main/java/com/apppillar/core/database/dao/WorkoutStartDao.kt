package com.apppillar.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.apppillar.core.database.entity.CompletedExerciseEntity
import com.apppillar.core.database.entity.CompletedSetEntity
import com.apppillar.core.database.entity.CompletedWorkoutEntity
import com.apppillar.core.database.entity.relation.CompletedWorkoutWithExercises
import com.apppillar.core.model.MaxWeightStat
import com.apppillar.core.model.SetsStat
import com.apppillar.core.model.VolumeStat
import com.apppillar.core.model.WorkoutStat

@Dao
interface WorkoutStartDao {
    @Insert
    suspend fun insertCompletedWorkout(entity: CompletedWorkoutEntity): Long
    @Insert
    suspend fun insertCompletedExercises(entities: List<CompletedExerciseEntity>): List<Long>
    @Insert
    suspend fun insertCompletedSets(entities: List<CompletedSetEntity>)

    @Transaction
    @Query("SELECT * FROM completed_workout_list WHERE id = :id")
    suspend fun getCompletedWorkoutById(id: Long): CompletedWorkoutWithExercises

    @Query("""
    SELECT strftime('%Y-%m-%d', datetime(timestamp / 1000, 'unixepoch', 'localtime')) AS date, COUNT(*) as count
    FROM completed_workout_list
    WHERE timestamp >= :startMillis
    GROUP BY date
""")
    suspend fun getWorkoutCountPerDay(startMillis: Long): List<WorkoutStat>

    @Query("""
    SELECT strftime('%Y-%m-%d', datetime(w.timestamp / 1000, 'unixepoch', 'localtime')) AS date,
           SUM(CAST(s.weight AS FLOAT) * CAST(s.reps AS INT)) AS totalVolume
    FROM completed_workout_list AS w
    JOIN completed_exercise_list AS e ON e.completedWorkoutId = w.id
    JOIN completed_set_list AS s ON s.exerciseId = e.id
    WHERE s.isCompleted = 1 AND w.timestamp >= :startMillis
    GROUP BY date
""")
    suspend fun getVolumeStats(startMillis: Long): List<VolumeStat>

    @Query("""
    SELECT strftime('%Y-%m-%d', datetime(w.timestamp / 1000, 'unixepoch', 'localtime')) AS date,
           COUNT(s.id) AS totalSets
    FROM completed_workout_list AS w
    JOIN completed_exercise_list AS e ON e.completedWorkoutId = w.id
    JOIN completed_set_list AS s ON s.exerciseId = e.id
    WHERE s.isCompleted = 1 AND w.timestamp >= :startMillis
    GROUP BY date
    ORDER BY date
""")
    suspend fun getSetsStats(startMillis: Long): List<SetsStat>

    @Query("""
    SELECT strftime('%Y-%m-%d', datetime(w.timestamp / 1000, 'unixepoch', 'localtime')) AS date,
           MAX(CAST(s.weight AS FLOAT)) AS maxWeight
    FROM completed_workout_list AS w
    JOIN completed_exercise_list AS e ON e.completedWorkoutId = w.id
    JOIN completed_set_list AS s ON s.exerciseId = e.id
    WHERE s.isCompleted = 1 AND w.timestamp >= :startMillis
    GROUP BY date
    ORDER BY date
""")
    suspend fun getMaxWeightStats(startMillis: Long): List<MaxWeightStat>

    @Query("SELECT COUNT(*) FROM completed_workout_list")
    suspend fun countCompletedWorkouts(): Int
}