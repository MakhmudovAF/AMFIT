package com.apppillar.feature_nutrition.presentation.product_search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apppillar.core.ResourcesProvider
import com.apppillar.core.database.entity.ProductLibraryEntity
import com.apppillar.feature_nutrition.R
import com.apppillar.feature_nutrition.databinding.ItemProductBinding

class ProductAdapter(
    private val resourcesProvider: ResourcesProvider,
    private val onClick: (ProductLibraryEntity) -> Unit
) : ListAdapter<ProductLibraryEntity, ProductAdapter.ProductViewHolder>(DiffCallback) {

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductLibraryEntity) = with(binding) {
            textViewName.text = item.name
            textViewInfo.text =
                resourcesProvider.getString(R.string.cal) + ": ${item.caloriesPer100g} • " + resourcesProvider.getString(
                    R.string.p
                ) + ": ${item.proteinPer100g} • " + resourcesProvider.getString(R.string.f) + ": ${item.fatPer100g} • " + resourcesProvider.getString(
                    R.string.c
                ) + ": ${item.carbsPer100g}"
            root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ProductLibraryEntity>() {
            override fun areItemsTheSame(old: ProductLibraryEntity, new: ProductLibraryEntity) =
                old.id == new.id

            override fun areContentsTheSame(old: ProductLibraryEntity, new: ProductLibraryEntity) =
                old == new
        }
    }
}