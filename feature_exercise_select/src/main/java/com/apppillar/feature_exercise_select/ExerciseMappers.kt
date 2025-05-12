package com.apppillar.feature_exercise_select

import com.apppillar.core.model.ExerciseDto
import com.apppillar.core.model.ExerciseSelect

fun ExerciseDto.toEntity() = ExerciseSelectEntity(id, name, bodyPart, equipment, gifUrl)

fun ExerciseSelectEntity.toModel() = ExerciseSelect(id, name, bodyPart, equipment, gifUrl)