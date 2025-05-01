package com.apppillar.feature_auth.presentation.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.apppillar.feature_auth.R
import com.apppillar.feature_auth.databinding.FragmentRegisterBinding
import com.apppillar.feature_auth.presentation.login.state.LoginUiState
import com.apppillar.feature_auth.presentation.register.state.RegisterUiState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Сброс ошибок при вводе текста
        binding.textInputEditName.doAfterTextChanged {
            binding.textInputLayoutName.isErrorEnabled = false
            binding.textInputLayoutName.error = null
        }

        binding.textInputEditEmail.doAfterTextChanged {
            binding.textInputLayoutEmail.isErrorEnabled = false
            binding.textInputLayoutEmail.error = null
        }

        binding.textInputEditPassword.doAfterTextChanged {
            binding.textInputLayoutPassword.isErrorEnabled = false
            binding.textInputLayoutPassword.error = null
        }

        // Кнопка "Зарегистрироваться"
        binding.materialButtonSignUp.setOnClickListener {
            val name = binding.textInputEditName.text.toString().trim()
            val email = binding.textInputEditEmail.text.toString().trim()
            val password = binding.textInputEditPassword.text.toString().trim()

            viewModel.register(name, email, password)
        }

        // Наблюдение за состоянием
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is RegisterUiState.Idle -> binding.loadingOverlay.visibility = View.GONE
                    is RegisterUiState.Loading -> binding.loadingOverlay.visibility = View.VISIBLE

                    is RegisterUiState.Success -> {
                        binding.loadingOverlay.visibility = View.GONE
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }

                    is RegisterUiState.Error -> {
                        binding.loadingOverlay.visibility = View.GONE
                        Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                    }

                    is RegisterUiState.Message -> {
                        binding.loadingOverlay.visibility = View.GONE
                        Toast.makeText(requireContext(), state.content, Toast.LENGTH_LONG).show()
                        viewModel.resetState()
                    }

                    is RegisterUiState.ValidationFailed -> {
                        binding.loadingOverlay.visibility = View.GONE
                        binding.textInputLayoutName.error = state.validationError.nameError
                        binding.textInputLayoutEmail.error = state.validationError.emailError
                        binding.textInputLayoutPassword.error = state.validationError.passwordError
                    }
                }
            }
        }

        // Переход на экран входа
        binding.materialTextViewSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}