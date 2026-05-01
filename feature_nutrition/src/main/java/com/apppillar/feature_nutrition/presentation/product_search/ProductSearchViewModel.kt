package com.apppillar.feature_nutrition.presentation.product_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apppillar.core.database.dao.ProductLibraryDao
import com.apppillar.core.database.entity.ProductLibraryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductSearchViewModel @Inject constructor(
    private val dao: ProductLibraryDao
) : ViewModel() {

    private val _query = MutableStateFlow("")
    private val _category = MutableStateFlow<String?>(null)

    private val _allProducts = MutableStateFlow<List<ProductLibraryEntity>>(emptyList())
    private val _categories = MutableStateFlow<List<String>>(emptyList())

    val products: StateFlow<List<ProductLibraryEntity>> = combine(
        _query, _category, _allProducts
    ) { query, category, all ->
        all.filter {
            (query.isBlank() || it.name.contains(query, ignoreCase = true)) &&
                    (category == null || it.category == category)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    init {
        viewModelScope.launch {
            _allProducts.value = dao.getAll()
            _categories.value = dao.getAllCategories().sorted()
        }
    }

    fun setQuery(value: String) {
        _query.value = value
    }

    fun setCategory(value: String?) {
        _category.value = value
    }

    fun clearCategory() {
        _category.value = null
    }
}