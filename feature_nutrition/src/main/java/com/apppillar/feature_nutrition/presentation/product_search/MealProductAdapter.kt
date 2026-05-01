package com.apppillar.feature_nutrition.presentation.product_search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apppillar.core.ResourcesProvider
import com.apppillar.core.database.entity.MealProductEntity
import com.apppillar.feature_nutrition.R
import com.apppillar.feature_nutrition.databinding.ItemMealProductBinding

class MealProductAdapter(
    private val resourcesProvider: ResourcesProvider,
    private var items: List<MealProductEntity> = emptyList(),
    private val onDelete: (Long) -> Unit
) : RecyclerView.Adapter<MealProductAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemMealProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MealProductEntity) = with(binding) {
            textViewProductName.text = item.name
            textViewGrams.text = "${item.grams} " + resourcesProvider.getString(R.string.g)
            textViewMacros.text =
                resourcesProvider.getString(R.string.cal) + ": ${item.calories} • " + resourcesProvider.getString(
                    R.string.p
                ) + ": ${item.protein} • " + resourcesProvider.getString(R.string.f) + ": ${item.fat} • " + resourcesProvider.getString(
                    R.string.c
                ) + ": ${item.carbs}"
            buttonDelete.setOnClickListener { onDelete(item.id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemMealProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position])

    fun submitList(newItems: List<MealProductEntity>) {
        items = newItems
        notifyDataSetChanged()
    }
}