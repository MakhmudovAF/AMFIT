package com.apppillar.feature_auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apppillar.core.storage.DataStorePrefs
import com.apppillar.feature_auth.domain.usecase.ForgotPasswordUseCase
import com.apppillar.feature_auth.domain.usecase.LoginUserUseCase
import com.apppillar.feature_auth.domain.validation.LoginValidator
import com.apppillar.feature_auth.presentation.login.state.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val dataStorePrefs: DataStorePrefs
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(email: String, password: String) {
        val validation = LoginValidator.validate(email, password)
        if (validation.hasError) {
            _uiState.value = LoginUiState.ValidationFailed(validation)
            return
        }

        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            val result = loginUserUseCase(email, password)
            if (result.isSuccess) {
                val data = result.getOrNull()
                if (data != null) {
                    dataStorePrefs.saveToken(data.token)
                    dataStorePrefs.saveUsername(data.username)
                }
                _uiState.value = LoginUiState.Success
            } else {
                val message = result.exceptionOrNull()?.message ?: "Неизвестная ошибка"
                _uiState.value = LoginUiState.Error(message)
            }
        }
    }

    fun sendResetRequest(email: String) {
        viewModelScope.launch {
            val result = forgotPasswordUseCase(email)
            _uiState.value = if (result.isSuccess) {
                LoginUiState.Message("Письмо отправлено")
            } else {
                val message = result.exceptionOrNull()?.localizedMessage ?: "Ошибка отправки"
                LoginUiState.Message(message)
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}