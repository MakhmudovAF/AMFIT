package com.apppillar.feature_workout_edit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class WorkoutEditHeaderAdapter(
    private var title: String,
    private val onTitleChanged: (String) -> Unit
) : RecyclerView.Adapter<WorkoutEditHeaderAdapter.HeaderViewHolder>() {

    private var validationActive = false
    private lateinit var titleInput: TextInputEditText
    var titleInputLayout: TextInputLayout? = null
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HeaderViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_workout_edit_header, parent, false)
        )

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind(title)
    }

    override fun getItemCount(): Int = 1

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            titleInputLayout = view.findViewById(R.id.text_input_layout_workout_title)
            titleInput = view.findViewById(R.id.edit_text_workout_title)

            titleInput.doAfterTextChanged {
                title = it.toString()
                onTitleChanged(title)

                if (validationActive) {
                    titleInputLayout?.apply {
                        error = if (title.isBlank()) "Title is required" else null
                        isErrorEnabled = !error.isNullOrBlank()
                    }
                }
            }
        }

        fun bind(currentTitle: String) {
            if (!titleInput.hasFocus() &&
                titleInput.text.toString() != currentTitle) {
                titleInput.setText(currentTitle)
            }
        }
    }

    fun updateTitle(newTitle: String) {
        title = newTitle
        notifyItemChanged(0)
    }

    fun validate(): Boolean {
        validationActive = true
        val isValid = title.isNotBlank()
        titleInputLayout?.error = if (!isValid) "Title is required" else null
        return isValid
    }
}