package com.apppillar.feature_home.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apppillar.feature_home.R
import com.apppillar.feature_home.databinding.ItemCompletedWorkoutBinding
import com.apppillar.feature_home.domain.model.CompletedWorkout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CompletedWorkoutAdapter(
    private val resourcesProvider: com.apppillar.core.ResourcesProvider,
    private val onClick: (CompletedWorkout) -> Unit
) : ListAdapter<CompletedWorkout, CompletedWorkoutAdapter.WorkoutViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding = ItemCompletedWorkoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class WorkoutViewHolder(
        private val binding: ItemCompletedWorkoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(workout: CompletedWorkout) {
            binding.materialTextViewTitle.text = workout.title
            binding.materialTextViewDuration.text = resourcesProvider.getString(R.string.duration) + workout.duration
            binding.materialTextViewDate.text = resourcesProvider.getString(R.string.date) + workout.date

            binding.root.setOnClickListener {
                onClick(workout)
            }
        }

        private fun formatDate(timestamp: Long): String {
            val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            return formatter.format(Date(timestamp))
        }

        private fun formatDuration(seconds: Int): String {
            val m = seconds / 60
            val s = seconds % 60
            return String.format("%d:%02d", m, s)
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<CompletedWorkout>() {
            override fun areItemsTheSame(old: CompletedWorkout, new: CompletedWorkout): Boolean =
                old.id == new.id

            override fun areContentsTheSame(old: CompletedWorkout, new: CompletedWorkout): Boolean =
                old == new
        }
    }
}