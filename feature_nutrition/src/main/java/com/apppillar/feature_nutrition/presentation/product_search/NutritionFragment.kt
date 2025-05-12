package com.apppillar.feature_nutrition.presentation.product_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.apppillar.core.database.entity.MealType
import com.apppillar.core.model.ProductSelection
import com.apppillar.feature_nutrition.R
import com.apppillar.feature_nutrition.databinding.FragmentNutritionBinding
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

@AndroidEntryPoint
class NutritionFragment : Fragment() {

    private var _binding: FragmentNutritionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NutritionViewModel by viewModels()

    private lateinit var breakfastAdapter: MealProductAdapter
    private lateinit var lunchAdapter: MealProductAdapter
    private lateinit var dinnerAdapter: MealProductAdapter
    private lateinit var snackAdapter: MealProductAdapter

    private var pendingMealType: MealType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        // Получаем продукт из диалога
        /*setFragmentResultListener("product_selected") { _, bundle ->
            val selected = bundle.getParcelable<ProductSelection>("product")
            val type = pendingMealType ?: return@setFragmentResultListener
            if (selected != null) {
                Log.e(":", "onCreate: ", )
                viewModel.addProductToMeal(type, selected)
            }
        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNutritionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        setupAdapters()
        setupAddButtons()
        observeState()
        observeNutritionTargets()

        // Показывать/скрывать кнопку "Сбросить фильтр"
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isDateFiltered.collectLatest { filtered ->
                    binding.materialToolbar.menu.findItem(R.id.action_reset_filter)?.isVisible = filtered
                }
            }
        }

        // Обновлять отображение даты в тулбаре
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedDate.collectLatest { date ->
                    val formatted = date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                    binding.materialToolbar.menu.findItem(R.id.text_select_date)?.title = formatted
                }
            }
        }

        findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<ProductSelection>("selected_product")
            ?.observe(viewLifecycleOwner) { selected ->
                val type = pendingMealType ?: return@observe
                viewModel.addProductToMeal(type, selected)
                findNavController().currentBackStackEntry?.savedStateHandle?.remove<ProductSelection>("selected_product")
            }
    }

    private fun setupToolbar() {
        binding.materialToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_select_date -> {
                    showDatePicker()
                    true
                }
                R.id.action_reset_filter -> {
                    viewModel.resetDateFilter()
                    true
                }
                else -> false
            }
        }
    }

    private fun showDatePicker() {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Выберите дату")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        picker.show(parentFragmentManager, "nutrition_date_picker")

        picker.addOnPositiveButtonClickListener { millis ->
            val selectedDate = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

            viewModel.setDate(selectedDate)
        }
    }

    private fun setupAdapters() {
        breakfastAdapter = MealProductAdapter { id -> viewModel.deleteMealProduct(id) }
        lunchAdapter = MealProductAdapter { id -> viewModel.deleteMealProduct(id) }
        dinnerAdapter = MealProductAdapter { id -> viewModel.deleteMealProduct(id) }
        snackAdapter = MealProductAdapter { id -> viewModel.deleteMealProduct(id) }

        binding.containerBreakfast.textMealType.text = "Завтрак"
        binding.containerLunch.textMealType.text = "Обед"
        binding.containerDinner.textMealType.text = "Ужин"
        binding.containerSnack.textMealType.text = "Перекус"


        binding.containerBreakfast.recyclerViewMealProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.containerLunch.recyclerViewMealProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.containerDinner.recyclerViewMealProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.containerSnack.recyclerViewMealProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.containerBreakfast.recyclerViewMealProducts.adapter = breakfastAdapter
        binding.containerLunch.recyclerViewMealProducts.adapter = lunchAdapter
        binding.containerDinner.recyclerViewMealProducts.adapter = dinnerAdapter
        binding.containerSnack.recyclerViewMealProducts.adapter = snackAdapter
    }

    private fun setupAddButtons() {
        binding.containerBreakfast.buttonAddProduct.setOnClickListener {
            pendingMealType = MealType.BREAKFAST
            openProductSearch()
        }
        binding.containerLunch.buttonAddProduct.setOnClickListener {
            pendingMealType = MealType.LUNCH
            openProductSearch()
        }
        binding.containerDinner.buttonAddProduct.setOnClickListener {
            pendingMealType = MealType.DINNER
            openProductSearch()
        }
        binding.containerSnack.buttonAddProduct.setOnClickListener {
            pendingMealType = MealType.SNACK
            openProductSearch()
        }
    }

    private fun openProductSearch() {
        findNavController().navigate("amfit://productSearch".toUri())
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                //binding.textViewDate.text = formatDate(state.selectedDate)

                breakfastAdapter.submitList(state.meals[MealType.BREAKFAST]?.products ?: emptyList())
                lunchAdapter.submitList(state.meals[MealType.LUNCH]?.products ?: emptyList())
                dinnerAdapter.submitList(state.meals[MealType.DINNER]?.products ?: emptyList())
                snackAdapter.submitList(state.meals[MealType.SNACK]?.products ?: emptyList())
            }
        }
    }

    private fun observeNutritionTargets() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(viewModel.uiState, viewModel.nutritionTarget) { state, target -> Pair(state, target) }
                    .collectLatest { (state, target) ->
                        if (target != null) {
                            binding.progressCalories.max = target.calories
                            binding.progressProtein.max = target.proteinGrams
                            binding.progressFat.max = target.fatGrams
                            binding.progressCarbs.max = target.carbsGrams

                            binding.progressCalories.progress = state.summary.calories
                            binding.progressProtein.progress = state.summary.protein.toInt()
                            binding.progressFat.progress = state.summary.fat.toInt()
                            binding.progressCarbs.progress = state.summary.carbs.toInt()

                            binding.textCaloriesSummary.text =
                                "${state.summary.calories} / ${target.calories} ккал"
                            binding.textProteinSummary.text =
                                "${state.summary.protein.toInt()} / ${target.proteinGrams} г"
                            binding.textFatSummary.text =
                                "${state.summary.fat.toInt()} / ${target.fatGrams} г"
                            binding.textCarbsSummary.text =
                                "${state.summary.carbs.toInt()} / ${target.carbsGrams} г"
                        }
                    }
            }
        }
    }


    private fun formatDate(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }

    private fun currentDayStart(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}