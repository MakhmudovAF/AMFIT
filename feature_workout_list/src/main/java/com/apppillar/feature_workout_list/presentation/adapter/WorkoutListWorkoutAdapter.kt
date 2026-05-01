package com.apppillar.feature_workout.presentation.workout_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apppillar.feature_workout_list.R
import com.apppillar.feature_workout_list.domain.model.Workout
import com.google.android.material.button.MaterialButton

class WorkoutListWorkoutAdapter(
    private val onStartClick: (Workout) -> Unit,
    private val onEditClick: (Workout) -> Unit,
    private val onDeleteClick: (Workout) -> Unit
) : ListAdapter<Workout, WorkoutListWorkoutAdapter.WorkoutViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout_list_workout, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val workoutName: TextView = itemView.findViewById(R.id.workoutName)
        private val startButton: MaterialButton = itemView.findViewById(R.id.workoutStart)
        private val moreButton: ImageButton = itemView.findViewById(R.id.workoutMoreMenu)

        fun bind(workout: Workout) {
            workoutName.text = workout.title

            startButton.setOnClickListener {
                onStartClick(workout)
            }

            moreButton.setOnClickListener {
                showPopupMenu(moreButton, workout)
            }
        }

        private fun showPopupMenu(anchor: View, workout: Workout) {
            val popup = PopupMenu(anchor.context, anchor)
            popup.menuInflater.inflate(R.menu.menu_workout_item, popup.menu)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_edit -> {
                        onEditClick(workout)
                        true
                    }

                    R.id.action_delete -> {
                        onDeleteClick(workout)
                        true
                    }

                    else -> false
                }
            }
            popup.show()
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Workout>() {
            override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
                return oldItem == newItem
            }
        }
    }
}