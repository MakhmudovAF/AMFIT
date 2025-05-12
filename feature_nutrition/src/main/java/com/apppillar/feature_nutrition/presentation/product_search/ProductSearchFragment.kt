package com.apppillar.feature_nutrition.presentation.product_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.apppillar.core.model.ProductSelection
import com.apppillar.feature_nutrition.databinding.FragmentProductSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductSearchFragment : Fragment() {

    private var _binding: FragmentProductSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductSearchViewModel by viewModels()
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*// Получаем результат из InputWeightDialog
        setFragmentResultListener("product_selected") { _, bundle ->
            val selected = bundle.getParcelable<ProductSelection>("product")
            if (selected != null) {
                // Возвращаем его через SavedStateHandle
                findNavController().previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("selected_product", selected)

                findNavController().navigateUp()
            }
        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupSearch()
        observeCategories()
        observeProducts()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter { product ->
            //showInputDialog(product)
            val selection = ProductSelection(
                name = product.name,
                grams = 0,
                calories = product.caloriesPer100g,
                protein = product.proteinPer100g,
                fat = product.fatPer100g,
                carbs = product.carbsPer100g,
                category = product.category
            )
            InputWeightDialogFragment.newInstance(selection)
                .show(parentFragmentManager, "InputWeightDialog")
        }
        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewProducts.adapter = adapter
    }

    private fun setupSearch() {
        binding.editTextSearch.doAfterTextChanged {
            viewModel.setQuery(it.toString())
        }
    }

    private fun observeCategories() {
        lifecycleScope.launch {
            viewModel.categories.collect { categories ->
                val list = listOf("Все категории") + categories
                val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, list)
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerCategory.adapter = spinnerAdapter

                binding.spinnerCategory.setSelection(0)
                binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        if (position == 0) viewModel.clearCategory()
                        else viewModel.setCategory(list[position])
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }
            }
        }
    }

    private fun observeProducts() {
        lifecycleScope.launch {
            viewModel.products.collect { list ->
                adapter.submitList(list)
            }
        }
    }

    /*private fun showInputDialog(product: ProductLibraryEntity) {
        InputWeightDialogFragment.newInstance(product)
            .show(parentFragmentManager, "InputWeightDialog")
    }*/

    override fun onResume() {
        super.onResume()

        // Проверка: есть ли сохранённый результат в SavedStateHandle
        val selected = findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.get<ProductSelection>("selected_product")

        if (selected != null) {
            // Передаём результат назад
            findNavController().previousBackStackEntry
                ?.savedStateHandle
                ?.set("selected_product", selected)

            // Удаляем ключ, чтобы не сработало повторно
            findNavController().currentBackStackEntry
                ?.savedStateHandle
                ?.remove<ProductSelection>("selected_product")

            // Закрываем текущий фрагмент
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}