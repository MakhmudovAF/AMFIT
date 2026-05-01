package com.apppillar.feature_home.presentation.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apppillar.feature_home.R
import com.bumptech.glide.Glide

class CompletedWorkoutReadOnlyAdapter(
    private val exercises: List<ExerciseWithSetsUi>
) : RecyclerView.Adapter<CompletedWorkoutReadOnlyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout_detail_exercise, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = exercises.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.textView_startWorkout_exerciseName)
        private val bodyPart = view.findViewById<TextView>(R.id.textView_startWorkout_exerciseBodyPart)
        private val image = view.findViewById<ImageView>(R.id.imageView_detailWorkout_exercise)
        private val container = view.findViewById<LinearLayout>(R.id.linearLayout_startWorkout_exerciseSets)

        fun bind(item: ExerciseWithSetsUi) {
            name.text = item.name
            bodyPart.text = item.bodyPart
            Glide.with(image).load(item.imageUrl).into(image)
            container.removeAllViews()

            item.sets.forEach { set ->
                val setView = LayoutInflater.from(container.context)
                    .inflate(R.layout.item_workout_detail_exercise_set, container, false)

                val reps = setView.findViewById<EditText>(R.id.editText_detailWorkout_reps)
                val weight = setView.findViewById<EditText>(R.id.editText_detailWorkout_weight)
                val check = setView.findViewById<CheckBox>(R.id.materialCheckBox_done)

                reps.setText(set.reps)
                weight.setText(set.weight)
                check.isChecked = set.isCompleted

                reps.isEnabled = false
                weight.isEnabled = false
                check.isEnabled = false

                container.addView(setView)
            }
        }
    }
}
