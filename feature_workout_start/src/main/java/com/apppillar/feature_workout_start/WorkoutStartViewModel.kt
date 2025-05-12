package com.apppillar.feature_workout_start

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apppillar.feature_workout_start.domain.model.Exercise
import com.apppillar.feature_workout_start.domain.model.Set
import com.apppillar.feature_workout_start.domain.model.Workout
import com.apppillar.feature_workout_start.domain.usecase.AddCompletedWorkoutUseCase
import com.apppillar.feature_workout_start.domain.usecase.GetWorkoutByIdUseCase
import com.apppillar.feature_workout_start.domain.usecase.SaveWorkoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutStartViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getWorkoutById: GetWorkoutByIdUseCase,
    private val saveWorkout: SaveWorkoutUseCase,
    private val addCompletedWorkout: AddCompletedWorkoutUseCase
) : ViewModel() {

    private val _workout = MutableStateFlow(Workout())
    val workout: StateFlow<Workout> = _workout.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private val restTimers = mutableMapOf<Long, MutableStateFlow<Long>>()
    private val restTimerJobs = mutableMapOf<Long, Job>()

    private var timerJob: Job? = null

    init {
        startTimer()
    }

    fun getWorkoutForStart(workoutId: Long) {
        viewModelScope.launch {
            val workout = getWorkoutById(workoutId)
            _workout.value = workout

            workout.exercises.forEach { ex ->
                restTimers[ex.id] = MutableStateFlow(ex.restDurationSeconds.toLong())
            }
        }
    }

    fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                _duration.update { it + 1 }
            }
        }
    }

    fun getRestTimer(exerciseId: Long): StateFlow<Long> {
        return restTimers.getOrPut(exerciseId) {
            val initial = _workout.value.exercises.find { it.id == exerciseId }?.restDurationSeconds?.toLong() ?: 0L
            MutableStateFlow(initial)
        }
    }

    fun startRestTimer(exerciseId: Long) {
        restTimerJobs[exerciseId]?.cancel()

        val exercise = _workout.value.exercises.find { it.id == exerciseId } ?: return
        val duration = exercise.restDurationSeconds
        val flow = restTimers.getOrPut(exerciseId) { MutableStateFlow(duration.toLong()) }

        val job = viewModelScope.launch {
            flow.value = duration.toLong()
            while (flow.value > 0) {
                delay(1000L)
                flow.update { it - 1 }
            }
            flow.value = duration.toLong() // сброс после окончания
        }

        restTimerJobs[exerciseId] = job
    }

    fun setRestDuration(exerciseId: Long, durationSeconds: Int) {
        _workout.update {
            val updated = it.exercises.map { ex ->
                if (ex.id == exerciseId) ex.copy(restDurationSeconds = durationSeconds)
                else ex
            }
            it.copy(exercises = updated)
        }

        restTimers[exerciseId]?.value = durationSeconds.toLong()
    }

    fun updateRestTimerDisplay(exerciseId: Long, seconds: Int) {
        restTimers.getOrPut(exerciseId) { MutableStateFlow(0L) }.value = seconds.toLong()
    }

    fun addExercise(exercise: Exercise) {
        _workout.update {
            it.copy(exercises = it.exercises.toMutableList().apply { add(exercise) })
        }
        restTimers[exercise.id] = MutableStateFlow(exercise.restDurationSeconds.toLong())
    }

    fun deleteExercise(id: Long) {
        _workout.update {
            it.copy(exercises = it.exercises.filterNot { ex -> ex.id == id })
        }
        restTimerJobs.remove(id)?.cancel()
        restTimers.remove(id)
    }

    fun addSetToExercise(exerciseId: Long) {
        _workout.update {
            val updated = it.exercises.map { ex ->
                if (ex.id == exerciseId) {
                    ex.copy(sets = ex.sets.toMutableList().apply { add(Set()) })
                } else ex
            }
            it.copy(exercises = updated)
        }
    }

    fun deleteSetFromExercise(exerciseId: Long, setId: Long) {
        _workout.update {
            val updated = it.exercises.map { ex ->
                if (ex.id == exerciseId) {
                    ex.copy(sets = ex.sets.filterNot { set -> set.id == setId })
                } else ex
            }
            it.copy(exercises = updated)
        }
    }

    fun updateSetChecked(exerciseId: Long, setId: Long, isChecked: Boolean) {
        _workout.update {
            val updated = it.exercises.map { ex ->
                if (ex.id == exerciseId) {
                    ex.copy(sets = ex.sets.map { set ->
                        if (set.id == setId) set.copy(isChecked = isChecked) else set
                    })
                } else ex
            }
            it.copy(exercises = updated)
        }

        if (!isChecked) {
            restTimerJobs[exerciseId]?.cancel()

            val restSeconds = _workout.value.exercises
                .find { it.id == exerciseId }
                ?.restDurationSeconds ?: 0

            restTimers[exerciseId]?.value = restSeconds.toLong()
        }
    }

    fun completeWorkout(volume: Float, sets: Int, onFinished: () -> Unit) {
        viewModelScope.launch {
            val domain = _workout.value
            saveWorkout.invoke(domain)
            addCompletedWorkout(domain, _duration.value.toInt(), volume, sets)
            onFinished()
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        restTimerJobs.values.forEach { it.cancel() }
        super.onCleared()
    }
}