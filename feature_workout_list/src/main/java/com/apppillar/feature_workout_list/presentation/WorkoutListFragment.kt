package com.apppillar.feature_workout_list.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.apppillar.core.navigation.FragmentDataListener
import com.apppillar.feature_workout.presentation.workout_list.WorkoutListWorkoutAdapter
import com.apppillar.feature_workout_list.databinding.FragmentWorkoutListBinding
import com.apppillar.feature_workout_list.presentation.adapter.WorkoutListHeaderAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutListFragment : Fragment() {
    private var _binding: FragmentWorkoutListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WorkoutListViewModel by viewModels()

    private lateinit var headerAdapter: WorkoutListHeaderAdapter
    private lateinit var workoutAdapter: WorkoutListWorkoutAdapter
    private lateinit var concatAdapter: ConcatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWorkoutListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Boolean>("workout")
            ?.observe(viewLifecycleOwner) { created ->
                if (created == true) {
                    viewModel.getWorkoutList()
                    findNavController().currentBackStackEntry?.savedStateHandle?.remove<Boolean>("workout")
                }
            }

        setupRecyclerView()
        observeState()
    }

    private fun setupRecyclerView() {
        // Заголовок со вступлением и кнопкой "Create"
        headerAdapter = WorkoutListHeaderAdapter(onCreateClick = {
            findNavController().navigate("amfit://workoutCreate".toUri())
        })

        // Адаптер списка тренировок
        workoutAdapter = WorkoutListWorkoutAdapter(
            onStartClick = { workout ->
                /* findNavController().navigate(
                    WorkoutListFragmentDirections
                        .actionWorkoutListFragmentToStartWorkoutFragment(workout.id)
                )*/
                (requireActivity() as? FragmentDataListener)?.passWorkoutIdStart(workout.id)
            },
            onEditClick = { workout ->
                (requireActivity() as? FragmentDataListener)?.passWorkoutIdEdit(workout.id)
            },
            onDeleteClick = { workout ->
                viewModel.deleteWorkout(workout)
            }
        )

        // Сборка через ConcatAdapter
        concatAdapter = ConcatAdapter(headerAdapter, workoutAdapter)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = concatAdapter
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                workoutAdapter.submitList(state.workoutList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}