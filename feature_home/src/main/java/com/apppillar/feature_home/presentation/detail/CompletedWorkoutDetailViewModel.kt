package com.apppillar.feature_home.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apppillar.core.database.dao.WorkoutStartDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CompletedWorkoutDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val dao: WorkoutStartDao
) : ViewModel() {

    private val workoutId = checkNotNull(savedStateHandle.get<Long>("workoutId"))

    val uiState: StateFlow<CompletedWorkoutDetailUiState?> = flow {
        val data = dao.getCompletedWorkoutById(workoutId)
        emit(data.toUiState())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
