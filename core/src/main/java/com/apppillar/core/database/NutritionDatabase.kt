package com.apppillar.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.apppillar.core.database.dao.NutritionDao
import com.apppillar.core.database.dao.ProductLibraryDao
import com.apppillar.core.database.entity.MealEntity
import com.apppillar.core.database.entity.MealProductEntity
import com.apppillar.core.database.entity.ProductLibraryEntity

@Database(
    entities = [
        ProductLibraryEntity::class,
        MealEntity::class,
        MealProductEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NutritionDatabase : RoomDatabase() {

    abstract fun nutritionDao(): NutritionDao
    abstract fun productLibraryDao(): ProductLibraryDao

    companion object {
        const val DATABASE_NAME = "nutrition_db"
    }
}