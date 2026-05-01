package com.apppillar.feature_home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apppillar.core.storage.DataStorePrefs
import com.apppillar.feature_home.R
import com.apppillar.feature_home.domain.model.HomeState
import com.apppillar.feature_home.domain.usecase.GetCompletedWorkoutsUseCase
import com.apppillar.feature_home.domain.usecase.GetDailyStepsUseCase
import com.apppillar.feature_home.domain.usecase.GetGoalsUseCase
import com.apppillar.feature_home.domain.usecase.GetStepsForDate
import com.apppillar.feature_home.domain.usecase.GetWeeklyCompletedWorkoutsUseCase
import com.apppillar.feature_home.domain.usecase.GetWorkoutsForDate
import com.apppillar.feature_home.domain.usecase.SaveGoalUseCase
import com.apppillar.feature_home.util.GoalType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    //private val addCompletedWorkoutUseCase: AddCompletedWorkoutUseCase,
    private val resourcesProvider: com.apppillar.core.ResourcesProvider,
    private val getDailyStepsUseCase: GetDailyStepsUseCase,
    private val getCompletedWorkoutsUseCase: GetCompletedWorkoutsUseCase,
    private val getStepsForDate: GetStepsForDate,
    private val getWorkoutsForDate: GetWorkoutsForDate,
    private val getWeeklyCompletedWorkoutsUseCase: GetWeeklyCompletedWorkoutsUseCase,
    private val getGoalsUseCase: GetGoalsUseCase,
    private val saveGoalUseCase: SaveGoalUseCase,
    private val dataStorePrefs: DataStorePrefs
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _isDateFiltered = MutableStateFlow(false)
    val isDateFiltered = _isDateFiltered.asStateFlow()

    private var observeJob: Job? = null

    init {
        observeHomeData()
    }

    private fun observeHomeData() {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            val goals = getGoalsUseCase()
            val username = dataStorePrefs.username.firstOrNull() ?: resourcesProvider.getString(R.string.user)

            combine(
                getDailyStepsUseCase(),
                goals.dailyStepsGoal,
                getCompletedWorkoutsUseCase(),
                getWeeklyCompletedWorkoutsUseCase(),
                goals.completedWorkoutsGoal
            ) { steps, stepsGoal, workouts, weeklyWorkouts, workoutGoal ->
                val newState = _state.value.copy(
                    user = username,
                    dailySteps = steps,
                    stepsGoal = stepsGoal,
                    workouts = weeklyWorkouts,
                    workoutsGoal = workoutGoal,
                    completedWorkouts = workouts
                )

                newState.copy(
                    goalAchievedMessage = when {
                        steps >= stepsGoal && _state.value.dailySteps < stepsGoal -> resourcesProvider.getString(R.string.congrats_steps)
                        workouts.size >= workoutGoal && _state.value.workouts < workoutGoal -> resourcesProvider.getString(R.string.congrats_workouts)
                        else -> null
                    }
                )
            }.collect {
                _state.value = it
            }
        }
    }

    fun saveGoal(type: GoalType, value: Int) {
        viewModelScope.launch {
            saveGoalUseCase(type, value)
        }
    }

    /*fun addDummyWorkout() {
        viewModelScope.launch {
            for (i in 1..5) {
                for (j in 1..i) {
                    val workout = CompletedWorkout(
                        id = UUID.randomUUID().toString(),
                        title = "Тестовая тренировка",
                        duration = 40 + j,
                        date = "2025-05-0$i"
                    )
                    addCompletedWorkoutUseCase(workout)
                }
            }
        }
    }*/

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        reloadDataForDate(date)
    }

    fun reloadDataForDate(date: LocalDate) {
        observeJob?.cancel()
        _isDateFiltered.value = true
        observeJob = viewModelScope.launch {
            val username = dataStorePrefs.username.firstOrNull() ?: resourcesProvider.getString(R.string.user)
            val formattedDate = date.toString()

            val stepsFlow = getStepsForDate(formattedDate)
            val goals = getGoalsUseCase()
            val workoutsFlow = getWorkoutsForDate(formattedDate)

            combine(
                stepsFlow,
                goals.dailyStepsGoal,
                workoutsFlow,
                goals.completedWorkoutsGoal
            ) { steps, stepGoal, workouts, workoutGoal ->
                HomeState(
                    user = username,
                    dailySteps = steps,
                    stepsGoal = stepGoal,
                    workouts = workouts.size,
                    workoutsGoal = workoutGoal,
                    completedWorkouts = workouts
                )
            }.collect {
                _state.value = it
            }
        }
    }

    fun resetDateFilter() {
        _isDateFiltered.value = false
        _selectedDate.value = LocalDate.now()
        observeHomeData()
    }

    fun clearGoalMessage() {
        _state.value = _state.value.copy(goalAchievedMessage = null)
    }
}