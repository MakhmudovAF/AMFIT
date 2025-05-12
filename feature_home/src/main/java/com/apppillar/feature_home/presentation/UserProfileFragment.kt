package com.apppillar.feature_home.presentation

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.apppillar.core.model.Gender
import com.apppillar.core.model.Goal
import com.apppillar.core.model.UserProfile
import com.apppillar.core.storage.DataStorePrefs
import com.apppillar.feature_home.databinding.FragmentUserProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var dataStorePrefs: DataStorePrefs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupSpinners()

        binding.buttonSave.setOnClickListener {
            val gender = if (binding.radioMale.isChecked) Gender.MALE else Gender.FEMALE
            val age = binding.editAge.text.toString().toIntOrNull() ?: return@setOnClickListener
            val height = binding.editHeight.text.toString().toDoubleOrNull() ?: return@setOnClickListener
            val weight = binding.editWeight.text.toString().toDoubleOrNull() ?: return@setOnClickListener
            val activity = activityLevels[binding.spinnerActivity.selectedItemPosition]
            val goal = goals[binding.spinnerGoal.selectedItemPosition]

            val profile = UserProfile(
                gender = gender,
                age = age,
                weightKg = weight,
                heightCm = height,
                activityLevel = activity,
                goal = goal
            )

            lifecycleScope.launch {
                dataStorePrefs.saveUserProfile(profile)
                findNavController().navigateUp()
            }
        }
    }

    private val activityLevels = listOf(1.2, 1.375, 1.55, 1.725, 1.9)
    private val activityLabels = listOf(
        "Малоподвижный", "Лёгкая активность", "Средняя", "Высокая", "Очень высокая"
    )

    private val goals = listOf(Goal.LOSE_WEIGHT, Goal.MAINTAIN, Goal.GAIN_WEIGHT)
    private val goalLabels = listOf("Похудение", "Поддержание", "Набор массы")

    private fun setupSpinners() {
        binding.spinnerActivity.adapter = ArrayAdapter(
            requireContext(), R.layout.simple_spinner_dropdown_item, activityLabels
        )
        binding.spinnerGoal.adapter = ArrayAdapter(
            requireContext(), R.layout.simple_spinner_dropdown_item, goalLabels
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}