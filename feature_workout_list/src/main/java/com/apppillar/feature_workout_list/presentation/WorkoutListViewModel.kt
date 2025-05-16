package com.apppillar.feature_workout_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apppillar.feature_workout_list.domain.model.Workout
import com.apppillar.feature_workout_list.domain.model.WorkoutList
import com.apppillar.feature_workout_list.domain.usecase.DeleteWorkoutUseCase
import com.apppillar.feature_workout_list.domain.usecase.GetWorkoutListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutListViewModel @Inject constructor(
    private val deleteWorkoutUseCase: DeleteWorkoutUseCase,
    private val getWorkoutListUseCase: GetWorkoutListUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(WorkoutList())
    val state: StateFlow<WorkoutList> = _state.asStateFlow()

    init {
        getWorkoutList()
    }

    fun getWorkoutList() {
        viewModelScope.launch {
            val workoutList = getWorkoutListUseCase.invoke()
            _state.value = _state.value.copy(workoutList = workoutList)
        }
    }

    fun deleteWorkout(item: Workout) {
        viewModelScope.launch {
            try {
                deleteWorkoutUseCase(item.id)
                getWorkoutList()
            } catch (e: Exception) {
            }
        }
    }
}