package com.apppillar.core.database.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.apppillar.core.database.entity.ExerciseEntity
import com.apppillar.core.database.entity.WorkoutEntity

data class WorkoutWithExercises(
    @Embedded val workoutEntity: WorkoutEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutId",
        entity = ExerciseEntity::class
    )
    val exercises: List<ExerciseWithSets> = emptyList<ExerciseWithSets>()
)