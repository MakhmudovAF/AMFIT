package com.apppillar.feature_workout_start

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
import com.apppillar.core.model.ExerciseSelect
import com.apppillar.feature_workout_start.databinding.FragmentWorkoutStartBinding
import com.apppillar.feature_workout_start.domain.model.Exercise
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WorkoutStartFragment : Fragment() {
    private var _binding: FragmentWorkoutStartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WorkoutStartViewModel by viewModels()

    private lateinit var exerciseAdapter: WorkoutStartExerciseAdapter
    private lateinit var footerAdapter: WorkoutStartFooterAdapter
    private lateinit var concatAdapter: ConcatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val workoutId = arguments?.getLong("workoutId")
        if (workoutId != null) viewModel.getWorkoutForStart(workoutId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeNewExercise()
        setupToolbar()
        setupAdapters()
        observeWorkout()
        observeDuration()
    }

    private fun observeNewExercise() {
        findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<ExerciseSelect>("exercise")
            ?.observe(viewLifecycleOwner) { selected ->
                viewModel.addExercise(
                    Exercise(
                        name = selected.name,
                        bodyPart = selected.bodyPart,
                        imageUrl = selected.imageUrl
                    )
                )
            }
    }

    private fun setupToolbar() {
        binding.materialToolBarStartWorkout.apply {
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            setOnMenuItemClickListener {
                if (it.itemId == R.id.action_save) {
                    viewModel.completeWorkout(
                        binding.materialTextViewVolumeText.text.toString().toFloat(),
                        binding.materialTextViewSetText.text.toString().toInt()
                    ) {
                        findNavController().navigateUp()
                    }
                    true
                } else false
            }
        }
    }

    private fun setupAdapters() {
        exerciseAdapter = WorkoutStartExerciseAdapter(
            onDeleteExercise = viewModel::deleteExercise,
            onAddSet = viewModel::addSetToExercise,
            onDeleteSet = viewModel::deleteSetFromExercise,
            onStartRestTimer = viewModel::startRestTimer,
            getRestTimer = viewModel::getRestTimer,
            onRestDurationSelected = { exerciseId ->
                showRestDurationDialog(exerciseId)
            },
            onSetChecked = viewModel::updateSetChecked
        )

        footerAdapter = WorkoutStartFooterAdapter {
            findNavController().navigate("amfit://exerciseSelect".toUri())
        }

        concatAdapter = ConcatAdapter(exerciseAdapter, footerAdapter)

        binding.recyclerViewStartWorkout.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = concatAdapter
        }
    }

    private fun observeWorkout() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.workout.collect { draft ->
                exerciseAdapter.setItems(draft.exercises)
                binding.materialToolBarStartWorkout.title = draft.title

                val totalVolume = draft.exercises.sumOf { ex ->
                    ex.sets.filter { it.isChecked }.sumOf {
                        val w = it.weight.toDoubleOrNull() ?: 0.0
                        val r = it.reps.toIntOrNull() ?: 0
                        (w * r).toInt()
                    }
                }

                val completedSets = draft.exercises.sumOf { ex ->
                    ex.sets.count { it.isChecked }
                }

                binding.materialTextViewVolumeText.text = totalVolume.toString()
                binding.materialTextViewSetText.text = completedSets.toString()
            }
        }
    }

    private fun observeDuration() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.duration.collect { seconds ->
                binding.materialTextViewDurationText.text = formatDuration(seconds)
            }
        }
    }

    private fun showRestDurationDialog(exerciseId: Long) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_rest_duration, null)

        val minutesInput = dialogView.findViewById<EditText>(R.id.editText_minutes)
        val secondsInput = dialogView.findViewById<EditText>(R.id.editText_seconds)

        AlertDialog.Builder(requireContext())
            .setTitle("Set Rest Duration")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val minutes = minutesInput.text.toString().toIntOrNull() ?: 0
                val seconds = secondsInput.text.toString().toIntOrNull() ?: 0
                val total = minutes * 60 + seconds
                viewModel.setRestDuration(exerciseId, total)
                viewModel.updateRestTimerDisplay(exerciseId, total)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun formatDuration(seconds: Long): String {
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        return String.format("%02d:%02d:%02d", h, m, s)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}