package com.apppillar.feature_nutrition.data

import android.content.Context
import com.apppillar.core.database.dao.ProductLibraryDao
import com.apppillar.core.database.entity.ProductLibraryEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProductPreload @Inject constructor(
    private val dao: ProductLibraryDao,
    @ApplicationContext private val context: Context
) {
    suspend fun preloadIfEmpty() {
        if (dao.getAll().isNotEmpty()) return

        val inputStream = context.assets.open("products_1000_clean.json")
        val json = inputStream.bufferedReader().use { it.readText() }

        val type = object : TypeToken<List<ProductLibraryEntity>>() {}.type
        val products: List<ProductLibraryEntity> = Gson().fromJson(json, type)

        dao.insertAll(products)
    }
}