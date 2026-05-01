package com.apppillar.feature_workout_list.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apppillar.feature_workout_list.R
import com.google.android.material.button.MaterialButton

class WorkoutListHeaderAdapter(
    private val onCreateClick: () -> Unit
) : RecyclerView.Adapter<WorkoutListHeaderAdapter.HeaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout_list_header, parent, false)
        return HeaderViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = 1

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val createButton: MaterialButton = itemView.findViewById(R.id.materialButton)

        fun bind() {
            createButton.setOnClickListener {
                onCreateClick()
            }
        }
    }
}