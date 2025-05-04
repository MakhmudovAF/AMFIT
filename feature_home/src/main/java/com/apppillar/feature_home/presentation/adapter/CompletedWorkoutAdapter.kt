package com.apppillar.feature_home.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apppillar.feature_home.databinding.ItemCompletedWorkoutBinding
import com.apppillar.feature_home.domain.model.CompletedWorkout
import java.time.format.DateTimeFormatter

class CompletedWorkoutAdapter(
    private var items: List<CompletedWorkout> = emptyList()
) : RecyclerView.Adapter<CompletedWorkoutAdapter.WorkoutViewHolder>() {

    inner class WorkoutViewHolder(
        private val binding: ItemCompletedWorkoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(workout: CompletedWorkout) {
            binding.materialTextViewTitle.text = workout.title
            binding.materialTextViewDuration.text = "Duration: ${workout.duration} min."
            binding.materialTextViewDate.text = "Date: ${workout.date}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding = ItemCompletedWorkoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WorkoutViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun submitList(newList: List<CompletedWorkout>) {
        items = newList
        notifyDataSetChanged()
    }
}