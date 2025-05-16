package com.apppillar.feature_nutrition.presentation.product_search

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.apppillar.core.model.ProductSelection
import com.apppillar.feature_nutrition.R
import com.apppillar.feature_nutrition.databinding.DialogInputWeightBinding
import kotlin.math.roundToInt

class InputWeightDialogFragment : DialogFragment() {

    private var product: ProductSelection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        product = arguments?.getParcelable("product")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogInputWeightBinding.inflate(LayoutInflater.from(requireContext()))

        val builder = AlertDialog.Builder(requireContext())
            .setTitle(product?.name ?: "Продукт")
            .setView(binding.root)
            .setPositiveButton(getString(R.string.add)) { _, _ ->
                val grams = binding.editTextWeight.text.toString().toIntOrNull() ?: return@setPositiveButton
                product?.let {
                    val scaled = it.copy(
                        grams = grams,
                        calories = (it.calories * grams / 100.0).roundToInt(),
                        protein = (it.protein * grams / 100.0).toFloat(),
                        fat = (it.fat * grams / 100.0).toFloat(),
                        carbs = (it.carbs * grams / 100.0).toFloat()
                    )
                    findNavController().previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selected_product", scaled)

                    findNavController().navigateUp()
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)

        binding.editTextWeight.doAfterTextChanged {
            val grams = it.toString().toIntOrNull()
            val preview = grams?.let {
                product?.copy(
                    grams = it,
                    calories = (product!!.calories * it / 100.0).roundToInt(),
                    protein = (product!!.protein * it / 100.0).toFloat(),
                    fat = (product!!.fat * it / 100.0).toFloat(),
                    carbs = (product!!.carbs * it / 100.0).toFloat()
                )
            }
            binding.textViewSummary.text = preview?.let {
                getString(R.string.cal) + ": ${it.calories} • " + getString(
                    R.string.p
                ) + ": ${it.protein} • " + getString(R.string.f) + ": ${it.fat} • " + getString(
                    R.string.c
                ) + ": ${it.carbs}"
            } ?: ""
        }

        return builder.create()
    }

    companion object {
        fun newInstance(product: ProductSelection): InputWeightDialogFragment {
            return InputWeightDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("product", product)
                }
            }
        }
    }
}