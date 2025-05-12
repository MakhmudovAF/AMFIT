package com.apppillar.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductSelection(
    val name: String,
    val grams: Int,
    val calories: Int,
    val protein: Float,
    val fat: Float,
    val carbs: Float,
    val category: String?
) : Parcelable