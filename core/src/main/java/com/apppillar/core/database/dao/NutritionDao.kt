package com.apppillar.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.apppillar.core.database.entity.MealEntity
import com.apppillar.core.database.entity.MealProductEntity
import com.apppillar.core.database.entity.relation.MealWithProducts
import com.apppillar.core.model.CalorieStat
import com.apppillar.core.model.MacroStat

@Dao
interface NutritionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealProduct(product: MealProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealProducts(products: List<MealProductEntity>)

    @Query("DELETE FROM meal_products WHERE id = :id")
    suspend fun deleteProductById(id: Long)

    @Transaction
    @Query("SELECT * FROM meals WHERE date = :date")
    suspend fun getMealsWithProductsByDate(date: Long): List<MealWithProducts>

    @Query("""
    SELECT strftime('%Y-%m-%d', datetime(meals.date / 1000, 'unixepoch', 'localtime')) AS date, 
           SUM(meal_products.calories) AS totalCalories
    FROM meals
    LEFT JOIN meal_products ON meals.id = meal_products.mealId
    WHERE meals.date >= :startMillis
    GROUP BY date
""")
    suspend fun getCaloriesPerDay(startMillis: Long): List<CalorieStat>

    @Query("""
    SELECT strftime('%Y-%m-%d', datetime(meals.date / 1000, 'unixepoch', 'localtime')) AS date,
           SUM(meal_products.protein) AS totalProtein,
           SUM(meal_products.fat) AS totalFat,
           SUM(meal_products.carbs) AS totalCarbs
    FROM meals
    LEFT JOIN meal_products ON meals.id = meal_products.mealId
    WHERE meals.date >= :startMillis
    GROUP BY date
    ORDER BY date
""")
    suspend fun getMacrosPerDay(startMillis: Long): List<MacroStat>
}