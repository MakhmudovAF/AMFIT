package com.apppillar.feature_workout_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apppillar.feature_workout_edit.domain.model.Exercise
import com.apppillar.feature_workout_edit.domain.model.Set
import com.apppillar.feature_workout_edit.domain.model.Workout
import com.apppillar.feature_workout_edit.domain.usecase.GetWorkoutByIdUseCase
import com.apppillar.feature_workout_edit.domain.usecase.SaveWorkoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutEditViewModel @Inject constructor(
    private val getWorkoutByIdUseCase: GetWorkoutByIdUseCase,
    private val saveWorkoutUseCase: SaveWorkoutUseCase
) : ViewModel() {

    private val _workout = MutableStateFlow(Workout())
    val workout: StateFlow<Workout> = _workout.asStateFlow()

    private val restTimers = mutableMapOf<Long, MutableStateFlow<Long>>()

    fun setTitle(title: String) {
        _workout.update { it.copy(title = title) }
    }

    fun addExercise(exercise: Exercise) {
        _workout.update {
            it.copy(exercises = it.exercises.toMutableList().apply {
                add(exercise)
            })
        }
    }

    fun deleteExercise(id: Long) {
        _workout.update {
            it.copy(exercises = it.exercises.filterNot { ex -> ex.id == id })
        }
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

    fun saveWorkout(onFinished: () -> Unit) {
        viewModelScope.launch {
            val current = _workout.value
            if (current.title.isBlank()) return@launch

            val workout = Workout(
                id = current.id,
                title = current.title,
                exercises = current.exercises.map { ex ->
                    Exercise(
                        name = ex.name,
                        bodyPart = ex.bodyPart,
                        imageUrl = ex.imageUrl,
                        restDurationSeconds = ex.restDurationSeconds,
                        sets = ex.sets.map { set ->
                            Set(weight = set.weight, reps = set.reps)
                        }
                    )
                }
            )
            saveWorkoutUseCase.invoke(workout)
            onFinished()
        }
    }

    fun setRestDuration(exerciseId: Long, durationSeconds: Int) {
        _workout.update {
            val updated = it.exercises.map { ex ->
                if (ex.id == exerciseId) ex.copy(restDurationSeconds = durationSeconds)
                else ex
            }
            it.copy(exercises = updated)
        }
    }

    fun updateRestTimerDisplay(exerciseId: Long, seconds: Int) {
        restTimers.getOrPut(exerciseId) { MutableStateFlow(0L) }.value = seconds.toLong()
    }

    fun getWorkoutForEdit(id: Long) {
        viewModelScope.launch {
            val workout = getWorkoutByIdUseCase(id)
            _workout.value = workout
        }
    }
}