package com.apppillar.feature_home.presentation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.apppillar.feature_home.R
import com.apppillar.feature_home.databinding.FragmentHomeBinding
import com.apppillar.feature_home.presentation.adapter.CompletedWorkoutAdapter
import com.apppillar.feature_home.presentation.adapter.HomeHeaderAdapter
import com.apppillar.feature_home.service.StepCounterService
import com.apppillar.feature_home.util.GoalType
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import androidx.core.view.get
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var homeHeaderAdapter: HomeHeaderAdapter
    private lateinit var completedWorkoutAdapter: CompletedWorkoutAdapter
    private lateinit var concatAdapter: ConcatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        startStepCounterService()
        observeUiState()

        binding.materialToolbar.setNavigationOnClickListener {
            viewModel.addDummyWorkout()
            Snackbar.make(binding.root, "Добавлена тестовая тренировка", Snackbar.LENGTH_SHORT)
                .show()
        }

        binding.materialToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
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

        lifecycleScope.launchWhenStarted {
            viewModel.isDateFiltered.collectLatest { filtered ->
                val menu = binding.materialToolbar.menu
                menu.findItem(R.id.action_reset_filter)?.isVisible = filtered
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.selectedDate.collectLatest { date ->
                val menu = binding.materialToolbar.menu
                menu.findItem(R.id.text_select_date)?.title = date.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startStepCounterService() {
        val intent = Intent(requireContext(), StepCounterService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireContext().startForegroundService(intent)
        } else {
            requireContext().startService(intent)
        }
    }

    private fun setupRecyclerView() {
        homeHeaderAdapter = HomeHeaderAdapter(
            onDailyStepsClick = { showGoalInputDialog(GoalType.DAILY_STEPS) },
            onCompletedWorkoutsClick = { showGoalInputDialog(GoalType.COMPLETED_WORKOUTS) }
        )
        completedWorkoutAdapter = CompletedWorkoutAdapter()

        concatAdapter = ConcatAdapter(homeHeaderAdapter, completedWorkoutAdapter)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = concatAdapter
        }
    }

    private fun observeUiState() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collectLatest { state ->
                homeHeaderAdapter.submitState(state)
                completedWorkoutAdapter.submitList(state.completedWorkouts)

                // ПОЗДРАВЛЕНИЕ
                state.goalAchievedMessage?.let { message ->
                    Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
                    viewModel.clearGoalMessage()
                }
            }
        }
    }

    private fun showDatePicker() {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Выберите дату")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        picker.show(parentFragmentManager, "date_picker")

        picker.addOnPositiveButtonClickListener { millis ->
            val selectedDate = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

            viewModel.selectDate(selectedDate)
        }
    }

    private fun showGoalInputDialog(type: GoalType) {
        val context = requireContext()
        val inputLayout = TextInputLayout(context).apply {
            hint = type.hint
            setPadding(48, 24, 48, 24)
        }
        val editText = TextInputEditText(context).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
        }
        inputLayout.addView(editText)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(type.title)
            .setView(inputLayout)
            .setPositiveButton("Сохранить") { _, _ ->
                val value = editText.text.toString().toIntOrNull()
                if (value != null) {
                    lifecycleScope.launch {
                        viewModel.saveGoal(type, value)
                    }
                } else {
                    Snackbar.make(
                        binding.root,
                        "Введите корректное значение",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}