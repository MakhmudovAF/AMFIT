package com.apppillar.feature_workout_edit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class WorkoutEditFooterAdapter(
    private val onAddExercise: () -> Unit
) : RecyclerView.Adapter<WorkoutEditFooterAdapter.FooterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FooterViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_workout_edit_footer, parent, false)
        )

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: FooterViewHolder, position: Int) {
        holder.bind()
    }

    inner class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val button =
            view.findViewById<MaterialButton>(R.id.button_create_workout_add_exercise)

        fun bind() {
            button.setOnClickListener { onAddExercise() }
        }
    }
}