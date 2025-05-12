package com.apppillar.feature_workout_edit

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.apppillar.feature_workout_edit.domain.model.Exercise
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class WorkoutEditExerciseAdapter(
    private val onDeleteExercise: (Long) -> Unit,
    private val onAddSet: (Long) -> Unit,
    private val onDeleteSet: (Long, Long) -> Unit,
    private val onRestDurationClick: (exerciseId: Long) -> Unit
) : RecyclerView.Adapter<WorkoutEditExerciseAdapter.ExerciseViewHolder>() {

    private val items = mutableListOf<Exercise>()

    fun setItems(newItems: List<Exercise>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout_edit_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.text_view_workout_create_exercise_exercise_name)
        private val bodyPart: TextView = view.findViewById(R.id.text_view_workout_create_exercise_exercise_body_part)
        private val image: ImageView = view.findViewById(R.id.image_view_workout_create_exercise)
        private val deleteBtn: ImageButton = view.findViewById(R.id.image_button_workout_create_exercise_delete)
        private val addSetBtn: MaterialButton = view.findViewById(R.id.button_workout_create_add_set)
        private val setsContainer: LinearLayout = view.findViewById(R.id.linear_layout_workout_create_exercise_sets)
        private val restTimerText = view.findViewById<TextView>(R.id.text_view_workout_create_rest_timer)

        fun bind(exercise: Exercise) {
            name.text = exercise.name
            bodyPart.text = exercise.bodyPart
            if (!exercise.imageUrl.isNullOrBlank()) {
                Glide.with(image).load(exercise.imageUrl).into(image)
            } else {
                image.setImageResource(R.drawable.ic_baseline_close_24)
            }

            deleteBtn.setOnClickListener { onDeleteExercise(exercise.id) }
            addSetBtn.setOnClickListener { onAddSet(exercise.id) }

            Log.e("TAG", "bind: ${exercise.restDurationSeconds}", )

            // Отображение restDurationSeconds без подписки
            val min = exercise.restDurationSeconds / 60
            val sec = exercise.restDurationSeconds % 60
            restTimerText.text = "Rest Timer: ${min}m ${sec}s"

            // Установка новой длительности по клику
            restTimerText.setOnClickListener {
                onRestDurationClick(exercise.id)
            }

            setsContainer.removeAllViews()
            exercise.sets.forEachIndexed { index, set ->
                val setView = LayoutInflater.from(setsContainer.context)
                    .inflate(R.layout.item_workout_edit_exercise_set, setsContainer, false)

                val setNumber = setView.findViewById<TextView>(R.id.text_view_workout_create_set_number)
                val weightInput = setView.findViewById<TextInputEditText>(R.id.edit_text_workout_create_weight)
                val repsInput = setView.findViewById<TextInputEditText>(R.id.edit_text_workout_create_reps)
                val deleteSetBtn = setView.findViewById<ImageButton>(R.id.image_button_workout_create_set_delete)

                setNumber.text = (index + 1).toString()
                weightInput.setText(set.weight)
                repsInput.setText(set.reps)

                deleteSetBtn.setOnClickListener {
                    onDeleteSet(exercise.id, set.id)
                }

                weightInput.doAfterTextChanged {
                    set.weight = it.toString()
                }

                repsInput.doAfterTextChanged {
                    set.reps = it.toString()
                }

                setsContainer.addView(setView)
            }
        }
    }
}