package com.apppillar.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ExerciseSelect(
    val id: String,
    val name: String = "",
    val bodyPart: String = "",
    val equipment: String = "",
    val imageUrl: String? = null,
) : Parcelable