package com.apppillar.feature_home.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.apppillar.feature_home.databinding.FragmentCompletedWorkoutDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CompletedWorkoutDetailFragment : Fragment() {

    private var _binding: FragmentCompletedWorkoutDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CompletedWorkoutDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompletedWorkoutDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.materialToolBarDetailWorkout.setNavigationOnClickListener { findNavController().navigateUp() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (state != null) {
                    binding.materialToolBarDetailWorkout.title = state.name
                    binding.materialTextViewDetailDurationText.text = state.durationFormatted
                    binding.materialTextViewDetailVolumeText.text = state.totalVolume
                    binding.materialTextViewDetailSetText.text = state.totalSets

                    val comadapter = CompletedWorkoutReadOnlyAdapter(state.exercises)
                    binding.recyclerViewStartWorkout.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = comadapter
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
