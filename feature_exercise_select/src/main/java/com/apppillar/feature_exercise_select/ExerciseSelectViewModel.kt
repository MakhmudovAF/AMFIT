package com.apppillar.feature_exercise_select

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apppillar.core.model.ExerciseSelect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseSelectViewModel @Inject constructor(
    getExercises: GetExercisesUseCase,
    refreshExercises: RefreshExercisesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedBodyPart = MutableStateFlow<String?>(null)
    private val _selectedEquipment = MutableStateFlow<String?>(null)

    private val _allExercises = getExercises()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBodyParts: StateFlow<List<String>> = getExercises()
        .map { listOf("All") + it.map { it.bodyPart }.distinct() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allEquipment: StateFlow<List<String>> = getExercises()
        .map { listOf("All") + it.map { it.equipment }.distinct() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val exercises: StateFlow<List<ExerciseSelect>> = combine(
        _allExercises, _searchQuery, _selectedBodyPart, _selectedEquipment
    ) { list, query, bodyPart, equipment ->
        list.filter {
            (query.isBlank() || it.name.contains(query, true)) &&
                    (bodyPart == null || it.bodyPart.equals(bodyPart, true)) &&
                    (equipment == null || it.equipment.equals(equipment, true))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            try {
                refreshExercises()
            } catch (_: Exception) {} // offline
        }
    }

    fun setSearchQuery(query: String) = _searchQuery.tryEmit(query)
    fun selectBodyPart(part: String?) = _selectedBodyPart.tryEmit(part)
    fun selectEquipment(eq: String?) = _selectedEquipment.tryEmit(eq)
}
