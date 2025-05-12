package com.apppillar.core.database.entity

import androidx.room.TypeConverter

class MealTypeConverter {
    @TypeConverter
    fun fromMealType(type: MealType): String = type.name

    @TypeConverter
    fun toMealType(value: String): MealType = MealType.valueOf(value)
}