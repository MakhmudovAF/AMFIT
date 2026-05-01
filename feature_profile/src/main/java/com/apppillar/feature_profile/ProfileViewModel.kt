package com.apppillar.feature_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apppillar.core.storage.DataStorePrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dataStorePrefs: DataStorePrefs,
    private val getVolumeStats: GetVolumeStatsUseCase,
    private val getSetsStats: GetSetsStatsUseCase,
    private val getMaxWeightStats: GetMaxWeightStatsUseCase,
    private val getCalorieStats: GetCalorieStatsUseCase,
    private val getProteinStats: GetProteinStatsUseCase,
    private val getFatStats: GetFatStatsUseCase,
    private val getCarbsStats: GetCarbsStatsUseCase,
    private val countCompletedWorkoutsUseCase: CountCompletedWorkoutsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val username = dataStorePrefs.username.firstOrNull() ?: "Пользователь"

            _uiState.value = ProfileUiState(
                username = username,
                completedWorkoutsCount = countCompletedWorkoutsUseCase(),
                volumeStats = getVolumeStats(),
                setsStats = getSetsStats(),
                maxWeightStats = getMaxWeightStats(),
                calorieStats = getCalorieStats(),
                proteinStats = getProteinStats(),
                fatStats = getFatStats(),
                carbsStats = getCarbsStats()
            )
        }
    }
}

