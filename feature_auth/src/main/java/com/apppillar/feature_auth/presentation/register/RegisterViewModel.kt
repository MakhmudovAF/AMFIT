package com.apppillar.feature_auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apppillar.feature_auth.domain.usecase.RegisterUserUseCase
import com.apppillar.feature_auth.domain.validation.RegisterValidator
import com.apppillar.feature_auth.presentation.register.state.RegisterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun register(name: String, email: String, password: String) {
        val validation = RegisterValidator.validate(name, email, password)
        if (validation.hasError) {
            _uiState.value = RegisterUiState.ValidationFailed(validation)
            return
        }

        _uiState.value = RegisterUiState.Loading

        viewModelScope.launch {
            val result = registerUserUseCase(name, email, password)
            _uiState.value = if (result.isSuccess) {
                RegisterUiState.Success
            } else {
                RegisterUiState.Error(result.exceptionOrNull()?.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun resetState() {
        _uiState.value = RegisterUiState.Idle
    }
}