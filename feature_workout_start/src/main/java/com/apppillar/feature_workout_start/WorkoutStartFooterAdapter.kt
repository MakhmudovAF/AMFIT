package com.apppillar.feature_workout_start

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class WorkoutStartFooterAdapter(
    private val onAddExercise: () -> Unit
) : RecyclerView.Adapter<WorkoutStartFooterAdapter.FooterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FooterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout_start_footer, parent, false)
        return FooterViewHolder(view)
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: FooterViewHolder, position: Int) {
        holder.bind()
    }

    inner class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val button = view.findViewById<MaterialButton>(R.id.button_startWorkout_addExercise)

        fun bind() {
            button.setOnClickListener { onAddExercise() }
        }
    }
}