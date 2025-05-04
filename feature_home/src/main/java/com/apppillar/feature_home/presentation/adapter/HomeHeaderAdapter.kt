package com.apppillar.feature_home.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apppillar.feature_home.databinding.HomeRecyclerViewHeaderBinding
import com.apppillar.feature_home.domain.model.HomeState
import java.util.Calendar

class HomeHeaderAdapter(
    private val onDailyStepsClick: () -> Unit,
    private val onCompletedWorkoutsClick: () -> Unit
) : RecyclerView.Adapter<HomeHeaderAdapter.ViewHolder>() {
    private var uiState: HomeState = HomeState()

    fun submitState(newState: HomeState) {
        uiState = newState
        notifyItemChanged(0)
    }

    inner class ViewHolder(
        private val binding: HomeRecyclerViewHeaderBinding,
        private val onDailyStepsClick: () -> Unit,
        private val onCompletedWorkoutsClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(state: HomeState) = with(binding) {
            // Персональное приветствие
            materialTextViewGreeting.text = getGreeting(state.user)

            // Прогресс по шагам
            materialTextViewSteps.text = "${state.dailySteps}/${state.stepsGoal}"
            circularProgressIndicatorSteps.setProgressCompat(
                calculatePercentage(state.dailySteps, state.stepsGoal), true
            )

            // Прогресс по тренировкам (за неделю)
            materialTextViewWorkouts.text = "${state.workouts}/${state.workoutsGoal}"
            circularProgressIndicatorWorkouts.setProgressCompat(
                calculatePercentage(state.workouts, state.workoutsGoal), true
            )

            // Обработчики кликов
            circularProgressIndicatorSteps.setOnClickListener { onDailyStepsClick() }
            materialTextViewSteps.setOnClickListener { onDailyStepsClick() }

            circularProgressIndicatorWorkouts.setOnClickListener { onCompletedWorkoutsClick() }
            materialTextViewWorkouts.setOnClickListener { onCompletedWorkoutsClick() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            HomeRecyclerViewHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding, onDailyStepsClick, onCompletedWorkoutsClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(uiState)
    }

    override fun getItemCount() = 1

    private fun calculatePercentage(current: Int, goal: Int): Int {
        return if (goal == 0) 0 else (current.toFloat() / goal * 100).toInt().coerceAtMost(100)
    }

    private fun getGreeting(name: String): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 5..11 -> "Good Morning, $name!"
            in 12..17 -> "Good Afternoon, $name!"
            in 18..22 -> "Good Evening, $name!"
            else -> "Good Night, $name!"
        }
    }
}