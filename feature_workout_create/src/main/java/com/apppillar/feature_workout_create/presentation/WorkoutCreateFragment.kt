package com.apppillar.feature_workout_create.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.apppillar.core.ResourcesProvider
import com.apppillar.core.model.ExerciseSelect
import com.apppillar.feature_workout_create.R
import com.apppillar.feature_workout_create.data.mapper.WorkoutCreateMapper.toExercise
import com.apppillar.feature_workout_create.databinding.FragmentWorkoutCreateBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WorkoutCreateFragment : Fragment() {
    private var _binding: FragmentWorkoutCreateBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WorkoutCreateViewModel by viewModels()

    private lateinit var headerAdapter: WorkoutCreateHeaderAdapter
    private lateinit var exerciseAdapter: WorkoutCreateExerciseAdapter
    private lateinit var footerAdapter: WorkoutCreateFooterAdapter
    private lateinit var concatAdapter: ConcatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWorkoutCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<ExerciseSelect>("exercise")
            ?.observe(viewLifecycleOwner) { selected ->
                viewModel.addExercise(selected.toExercise())
                findNavController().currentBackStackEntry?.savedStateHandle?.remove<ExerciseSelect>("exercise")
            }
        setupToolbar()
        setupAdapters()
        observeWorkoutState()
    }

    private fun setupToolbar() {
        binding.materialToolBar.apply {
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            setOnMenuItemClickListener {
                if (it.itemId == R.id.action_save) {
                    val isValid = headerAdapter.validate()
                    if (!isValid) {
                        headerAdapter.titleInputLayout?.let { layout ->
                            binding.recyclerView.post {
                                binding.recyclerView.smoothScrollToPosition(0)
                                layout.requestFocus()
                            }
                        }
                        return@setOnMenuItemClickListener true
                    }

                    viewModel.saveWorkout {
                        findNavController().previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("workout", true)
                        findNavController().navigateUp()
                    }
                    true
                } else false
            }
        }
    }

    private fun setupAdapters() {
        headerAdapter = WorkoutCreateHeaderAdapter(ResourcesProvider(requireContext()), "") { title ->
            viewModel.setTitle(title)
        }

        exerciseAdapter = WorkoutCreateExerciseAdapter(
            ResourcesProvider(requireContext()),
            onDeleteExercise = viewModel::deleteExercise,
            onAddSet = viewModel::addSetToExercise,
            onDeleteSet = viewModel::deleteSetFromExercise,
            onRestDurationClick = { exerciseId -> showRestDurationDialog(exerciseId) },
        )

        footerAdapter = WorkoutCreateFooterAdapter {
            /*findNavController().navigate(
                WorkoutCreateFragmentDirections
                    .actionWorkoutCreateFragmentToExerciseSelectFragment("exercise_select_create")
            )*/
            findNavController().navigate("amfit://exerciseSelect".toUri())
        }

        concatAdapter = ConcatAdapter(headerAdapter, exerciseAdapter, footerAdapter)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = concatAdapter
        }
    }

    private fun showRestDurationDialog(exerciseId: Long) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_rest_duration, null)

        val minutesInput = dialogView.findViewById<EditText>(R.id.editText_minutes)
        val secondsInput = dialogView.findViewById<EditText>(R.id.editText_seconds)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.set_rest_duration))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                val minutes = minutesInput.text.toString().toIntOrNull() ?: 0
                val seconds = secondsInput.text.toString().toIntOrNull() ?: 0
                val total = minutes * 60 + seconds
                viewModel.setRestDuration(exerciseId, total)
                viewModel.updateRestTimerDisplay(exerciseId, total)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun observeWorkoutState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.workout.collect { workout ->
                headerAdapter.updateTitle(workout.title)
                exerciseAdapter.setItems(workout.exercises)
            }
        }
    }
}