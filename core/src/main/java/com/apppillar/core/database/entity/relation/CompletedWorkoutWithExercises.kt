package com.apppillar.core.database.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.apppillar.core.database.entity.CompletedExerciseEntity
import com.apppillar.core.database.entity.CompletedWorkoutEntity

data class CompletedWorkoutWithExercises(
    @Embedded val workout: CompletedWorkoutEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "completedWorkoutId",
        entity = CompletedExerciseEntity::class
    )
    val exercises: List<CompletedExerciseWithSets>
)