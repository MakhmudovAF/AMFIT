package com.apppillar.feature_workout_start

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.apppillar.core.ResourcesProvider
import com.apppillar.feature_workout_start.domain.model.Exercise
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkoutStartExerciseAdapter(
    private val resourcesProvider: ResourcesProvider,
    private val onDeleteExercise: (Long) -> Unit,
    private val onAddSet: (Long) -> Unit,
    private val onDeleteSet: (Long, Long) -> Unit,
    private val onStartRestTimer: (Long) -> Unit,
    private val getRestTimer: (Long) -> StateFlow<Long>,
    private val onRestDurationSelected: (exerciseId: Long) -> Unit,
    private val onSetChecked: (exerciseId: Long, setId: Long, isChecked: Boolean) -> Unit
) : RecyclerView.Adapter<WorkoutStartExerciseAdapter.ExerciseViewHolder>() {

    private val items = mutableListOf<Exercise>()

    fun setItems(newItems: List<Exercise>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout_start_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val name: TextView = view.findViewById(R.id.textView_startWorkout_exerciseName)
        private val bodyPart: TextView = view.findViewById(R.id.textView_startWorkout_exerciseBodyPart)
        private val equip: TextView = view.findViewById(R.id.textView_startWorkout_exerciseEquip)
        private val image: ImageView = view.findViewById(R.id.imageView_startWorkout_exercise)
        private val deleteBtn: ImageButton = view.findViewById(R.id.imageButton_startWorkout_exerciseDelete)
        private val addSetBtn: MaterialButton = view.findViewById(R.id.button_startWorkout_addSet)
        private val setsContainer: LinearLayout = view.findViewById(R.id.linearLayout_startWorkout_exerciseSets)
        private val restTimerText: TextView = view.findViewById(R.id.textView_startWorkout_restTimer)

        private var timerJob: Job? = null

        fun bind(ex: Exercise) {
            name.text = ex.name
            bodyPart.text = ex.bodyPart
            equip.text = ex.equipment
            if (!ex.imageUrl.isNullOrBlank()) {
                Glide.with(image).load(ex.imageUrl).into(image)
            } else {
                image.setImageResource(R.drawable.ic_placeholder)
            }

            deleteBtn.setOnClickListener { onDeleteExercise(ex.id) }
            addSetBtn.setOnClickListener { onAddSet(ex.id) }

            restTimerText.setOnClickListener {
                onRestDurationSelected(ex.id)
            }

            timerJob?.cancel()
            itemView.post {
                timerJob = itemView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                    getRestTimer(ex.id).collect { seconds ->
                        val min = seconds / 60
                        val sec = seconds % 60
                        restTimerText.text = resourcesProvider.getString(R.string.rest_timer) + "${min}m ${sec}s"
                    }
                }
            }

            setsContainer.removeAllViews()
            ex.sets.forEachIndexed { index, set ->
                val setView = LayoutInflater.from(setsContainer.context)
                    .inflate(R.layout.item_workout_start_exercise_set, setsContainer, false)

                val setNumber = setView.findViewById<TextView>(R.id.textView_startWorkout_setNumber)
                val weightInput = setView.findViewById<TextInputEditText>(R.id.editText_startWorkout_weight)
                val repsInput = setView.findViewById<TextInputEditText>(R.id.editText_startWorkout_reps)
                val deleteSetBtn = setView.findViewById<ImageButton>(R.id.imageButton_startWorkout_setDelete)
                val checkBox = setView.findViewById<MaterialCheckBox>(R.id.materialCheckBox_done)

                setNumber.text = (index + 1).toString()
                weightInput.setText(set.weight)
                repsInput.setText(set.reps)
                checkBox.isChecked = set.isChecked

                deleteSetBtn.setOnClickListener {
                    onDeleteSet(ex.id, set.id)
                }

                weightInput.doAfterTextChanged {
                    set.weight = it.toString()
                }

                repsInput.doAfterTextChanged {
                    set.reps = it.toString()
                }

                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    onSetChecked(ex.id, set.id, isChecked)
                    if (isChecked) onStartRestTimer(ex.id)
                }

                setsContainer.addView(setView)
            }
        }
    }
}