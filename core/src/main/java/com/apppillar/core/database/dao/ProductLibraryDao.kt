package com.apppillar.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apppillar.core.database.entity.ProductLibraryEntity

@Dao
interface ProductLibraryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductLibraryEntity>)

    @Query("SELECT * FROM products_library ORDER BY name ASC")
    suspend fun getAll(): List<ProductLibraryEntity>

    @Query("SELECT * FROM products_library WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    suspend fun search(query: String): List<ProductLibraryEntity>

    @Query("SELECT DISTINCT category FROM products_library WHERE category IS NOT NULL")
    suspend fun getAllCategories(): List<String>

    @Query("SELECT * FROM products_library WHERE category = :category ORDER BY name ASC")
    suspend fun filterByCategory(category: String): List<ProductLibraryEntity>
}