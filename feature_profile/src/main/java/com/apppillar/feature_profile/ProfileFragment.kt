package com.apppillar.feature_profile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.apppillar.feature_profile.databinding.FragmentProfileBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.color.MaterialColors
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //setupCharts()
        setupObservers()
        setupTabs()
        setupThemeSwitch()
        setupEditButton()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.textViewUsername.text = "${state.username}!"
                    /*renderWorkoutChart(state.workoutStats)
                    renderNutritionChart(state.calorieStats)
                    renderVolumeChart(state.volumeStats)*/
                    binding.textViewCompletedWorkouts.text =
                        getString(R.string.completed_workouts) + " ${state.completedWorkoutsCount}"
                    renderLineChart(binding.chartWorkouts, state.volumeStats, getString(R.string.volume))
                    renderLineChart(binding.chartNutrition, state.calorieStats, getString(R.string.calories))

                    binding.tabWorkoutStats.getTabAt(0)?.select()
                    binding.tabNutritionStats.getTabAt(0)?.select()
                }
            }
        }
    }

    private fun setupTabs() {
        binding.tabWorkoutStats.apply {
            addTab(newTab().setText(getString(R.string.volume)))
            addTab(newTab().setText(getString(R.string.sets)))
            addTab(newTab().setText(getString(R.string.max_weight)))
        }

        binding.tabNutritionStats.apply {
            addTab(newTab().setText(getString(R.string.calories)))
            addTab(newTab().setText(getString(R.string.protein)))
            addTab(newTab().setText(getString(R.string.fat)))
            addTab(newTab().setText(getString(R.string.carbs)))
        }

        val startDate = LocalDate.now().minusDays(6)

        listOf(binding.chartWorkouts, binding.chartNutrition).forEach {
            it.apply {
                description.isEnabled = false
                legend.isEnabled = false
                axisRight.isEnabled = false
                xAxis.granularity = 1f
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.valueFormatter = DayAxisFormatter(startDate)
            }
        }

        binding.tabWorkoutStats.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val state = viewModel.uiState.value
                when (tab.position) {
                    0 -> renderLineChart(binding.chartWorkouts, state.volumeStats, getString(R.string.volume))
                    1 -> renderLineChart(binding.chartWorkouts, state.setsStats, getString(R.string.sets))
                    2 -> renderLineChart(binding.chartWorkouts, state.maxWeightStats, getString(R.string.max_weight))
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.tabNutritionStats.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val state = viewModel.uiState.value
                when (tab.position) {
                    0 -> renderLineChart(binding.chartNutrition, state.calorieStats, getString(R.string.calories))
                    1 -> renderLineChart(binding.chartNutrition, state.proteinStats, getString(R.string.protein))
                    2 -> renderLineChart(binding.chartNutrition, state.fatStats, getString(R.string.fat))
                    3 -> renderLineChart(binding.chartNutrition, state.carbsStats, getString(R.string.carbs))
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun renderLineChart(chart: LineChart, data: List<Number>, label: String) {
        if (data.isEmpty()) {
            chart.clear()
            chart.invalidate()
            return
        }

        val entries = data.mapIndexed { index, value ->
            Entry(index.toFloat(), value.toFloat())
        }

        val context = chart.context
        val textColor = MaterialColors.getColor(context, com.google.android.material.R.attr.colorPrimary, Color.WHITE)

        val dataSet = LineDataSet(entries, label).apply {
            valueTextSize = 12f
            color = textColor
            setCircleColor(textColor)
            valueTextColor = textColor
            lineWidth = 2f
            circleRadius = 4f
        }

        chart.xAxis.textColor = textColor
        chart.axisLeft.textColor = textColor
        chart.axisRight.textColor = textColor
        chart.legend.textColor = textColor
        chart.description.textColor = textColor

        chart.data = LineData(dataSet)
        chart.invalidate()
    }

    /*private fun setupCharts() {
        val startDate = LocalDate.now().minusDays(6)

        binding.chartWorkouts.apply {
            description.isEnabled = false
            legend.isEnabled = false
            axisRight.isEnabled = false
            xAxis.granularity = 1f
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = DayAxisFormatter(startDate)
        }

        binding.chartNutrition.apply {
            description.isEnabled = false
            legend.isEnabled = false
            axisRight.isEnabled = false
            xAxis.granularity = 1f
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = DayAxisFormatter(startDate)
        }

        binding.chartVolume.apply {
            description.isEnabled = false
            legend.isEnabled = false
            axisRight.isEnabled = false
            xAxis.granularity = 1f
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = DayAxisFormatter(startDate)
        }
    }*/

    /*private fun renderWorkoutChart(data: List<Int>) {
        if (data.isEmpty()) {
            binding.chartWorkouts.clear()
            binding.chartWorkouts.invalidate()
            return
        }

        val entries = data.mapIndexed { index, count ->
            BarEntry(index.toFloat(), count.toFloat())
        }

        val dataSet = BarDataSet(entries, "Тренировки").apply {
            valueTextSize = 12f
        }

        binding.chartWorkouts.data = BarData(dataSet)
        binding.chartWorkouts.invalidate()
    }

    private fun renderNutritionChart(data: List<Int>) {
        if (data.isEmpty()) {
            binding.chartNutrition.clear()
            binding.chartNutrition.invalidate()
            return
        }

        val entries = data.mapIndexed { index, calories ->
            Entry(index.toFloat(), calories.toFloat())
        }

        val dataSet = LineDataSet(entries, "Калории").apply {
            valueTextSize = 12f
            circleRadius = 4f
            lineWidth = 2f
        }

        binding.chartNutrition.data = LineData(dataSet)
        binding.chartNutrition.invalidate()
    }

    private fun renderVolumeChart(data: List<Float>) {
        if (data.isEmpty()) {
            binding.chartVolume.clear()
            binding.chartVolume.invalidate()
            return
        }

        val entries = data.mapIndexed { index, volume ->
            Entry(index.toFloat(), volume)
        }

        val dataSet = LineDataSet(entries, "Общий объём").apply {
            valueTextSize = 12f
            circleRadius = 4f
            lineWidth = 2f
        }

        binding.chartVolume.data = LineData(dataSet)
        binding.chartVolume.invalidate()
    }*/


    private fun setupThemeSwitch() {
        val isDark = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        binding.switchTheme.isChecked = isDark

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    private fun setupEditButton() {
        binding.buttonEditProfile.setOnClickListener {
            findNavController().navigate("amfit://userProfileEdit".toUri())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
