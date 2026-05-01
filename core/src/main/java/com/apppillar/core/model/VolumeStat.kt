package com.apppillar.core.model

data class VolumeStat(
    val date: String,         // формат yyyy-MM-dd
    val totalVolume: Float    // сумма веса × повторения
)