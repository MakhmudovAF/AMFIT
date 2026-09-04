package com.example.amfit.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.amfit.data.WeekDay
import com.example.amfit.data.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val repository = WorkoutRepository()
    private val _uiState = MutableStateFlow(
        HomeUiState(
            weekDays = createWeekDays()
        )
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadWorkouts()
    }

    private fun loadWorkouts() {
        val workouts = repository.getFinishedWorkouts()

        _uiState.value = _uiState.value.copy(
            finishedWorkouts = workouts
        )
    }

    private fun createWeekDays(): List<WeekDay> {
        return listOf(
            WeekDay(
                letter = "S",
                isCompleted = true
            ),
            WeekDay(
                letter = "M",
                isCurrent = true
            ),
            WeekDay("T"),
            WeekDay("W"),
            WeekDay("T"),
            WeekDay("F"),
            WeekDay("S")
        )
    }
}
