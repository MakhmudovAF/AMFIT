package com.apppillar.feature_exercise_select

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apppillar.core.model.ExerciseSelect
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ExerciseSelectAdapter(
    private val onSelect: (ExerciseSelect) -> Unit
) : ListAdapter<ExerciseSelect, ExerciseSelectAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise_select, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.textView_exercise_name)
        private val info = view.findViewById<TextView>(R.id.textView_exercise_info)
        private val image = view.findViewById<ImageView?>(R.id.image_view_exercise)

        fun bind(exercise: ExerciseSelect) {
            name.text = exercise.name
            info.text = "${exercise.bodyPart} • ${exercise.equipment}"
            Log.e("TAG", "bind: ${exercise.imageUrl}", )

            image?.let {
                Glide.with(it.context)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .load(exercise.imageUrl)
                    .into(it)
            }

            itemView.setOnClickListener { onSelect(exercise) }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ExerciseSelect>() {
            override fun areItemsTheSame(old: ExerciseSelect, new: ExerciseSelect) =
                old.id == new.id

            override fun areContentsTheSame(old: ExerciseSelect, new: ExerciseSelect) =
                old == new
        }
    }
}
