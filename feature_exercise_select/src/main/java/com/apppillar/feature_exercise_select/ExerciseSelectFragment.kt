package com.apppillar.feature_exercise_select

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
import com.apppillar.feature_exercise_select.databinding.FragmentExerciseSelectBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExerciseSelectFragment : Fragment() {

    private var _binding: FragmentExerciseSelectBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExerciseSelectViewModel by viewModels()
    private lateinit var adapter: ExerciseSelectAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupSearch()
        setupSpinners()
        //setupFilters()
        observeExercises()
    }

    private fun setupRecyclerView() {
        adapter = ExerciseSelectAdapter { selected ->
            findNavController().previousBackStackEntry?.savedStateHandle?.set("exercise", selected)
            findNavController().navigateUp()
        }

        binding.recyclerViewExerciseList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewExerciseList.adapter = adapter
    }

    private fun setupSearch() {
        binding.searchEditText.doAfterTextChanged {
            viewModel.setSearchQuery(it.toString())
        }
    }

    private fun setupSpinners() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allBodyParts.collectLatest { bodyParts ->
                if (bodyParts.isNotEmpty()) {
                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, bodyParts)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerBodyPart.adapter = adapter
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allEquipment.collectLatest { equipment ->
                if (equipment.isNotEmpty()) {
                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, equipment)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerEquipment.adapter = adapter
                }
            }
        }

        binding.spinnerBodyPart.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selected = binding.spinnerBodyPart.selectedItem?.toString()
                viewModel.selectBodyPart(if (selected == "All") null else selected)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.spinnerEquipment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selected = binding.spinnerEquipment.selectedItem?.toString()
                viewModel.selectEquipment(if (selected == "All") null else selected)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }


    /*private fun setupFilters() {
        val bodyParts = viewModel.exercises.value.map { it.bodyPart }.distinct()
        val equipment = viewModel.exercises.value.map { it.equipment }.distinct()

        bodyParts.forEach { part ->
            val chip = Chip(requireContext()).apply {
                text = part
                isCheckable = true
                setOnClickListener {
                    viewModel.selectBodyPart(if (isChecked) part else null)
                }
            }
            binding.bodyPartChipGroup.addView(chip)
        }

        equipment.forEach { eq ->
            val chip = Chip(requireContext()).apply {
                text = eq
                isCheckable = true
                setOnClickListener {
                    viewModel.selectEquipment(if (isChecked) eq else null)
                }
            }
            binding.equipmentChipGroup.addView(chip)
        }
    }*/

    private fun observeExercises() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.exercises.collect { list ->
                adapter.submitList(list)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}