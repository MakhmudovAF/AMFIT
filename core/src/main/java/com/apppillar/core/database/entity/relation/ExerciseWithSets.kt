package com.apppillar.core.database.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.apppillar.core.database.entity.ExerciseEntity
import com.apppillar.core.database.entity.SetEntity

data class ExerciseWithSets(
    @Embedded val exerciseEntity: ExerciseEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "exerciseId",
        entity = SetEntity::class
    )
    val sets: List<SetEntity> = emptyList<SetEntity>()
)