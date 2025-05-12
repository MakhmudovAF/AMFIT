package com.apppillar.core.database.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.apppillar.core.database.entity.CompletedExerciseEntity
import com.apppillar.core.database.entity.CompletedSetEntity

data class CompletedExerciseWithSets(
    @Embedded val exercise: CompletedExerciseEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "exerciseId"
    )
    val sets: List<CompletedSetEntity>
)