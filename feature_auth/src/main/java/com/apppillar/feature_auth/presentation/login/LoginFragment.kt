package com.apppillar.feature_auth.presentation.login

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.apppillar.feature_auth.R
import com.apppillar.feature_auth.databinding.FragmentLoginBinding
import com.apppillar.feature_auth.presentation.login.state.LoginUiState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Сброс ошибок при вводе
        binding.textInputEditEmail.doAfterTextChanged {
            binding.textInputLayoutEmail.isErrorEnabled = false
            binding.textInputLayoutEmail.error = null
        }
        binding.textInputEditPassword.doAfterTextChanged {
            binding.textInputLayoutPassword.isErrorEnabled = false
            binding.textInputLayoutPassword.error = null
        }

        // Кнопка входа
        binding.materialButtonSignIn.setOnClickListener {
            val email = binding.textInputEditEmail.text.toString().trim()
            val password = binding.textInputEditPassword.text.toString().trim()
            viewModel.login(email, password)
        }

        binding.materialTextViewForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }

        // Переход на регистрацию
        binding.materialTextViewSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        // Наблюдение за состоянием
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is LoginUiState.Idle -> binding.loadingOverlay.visibility = View.GONE
                    is LoginUiState.Loading -> binding.loadingOverlay.visibility = View.VISIBLE
                    is LoginUiState.Success -> {
                        binding.loadingOverlay.visibility = View.GONE
                        findNavController().navigate("amfit://home".toUri())
                    }

                    is LoginUiState.Error -> {
                        binding.loadingOverlay.visibility = View.GONE
                        Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                    }

                    is LoginUiState.Message -> {
                        binding.loadingOverlay.visibility = View.GONE
                        Toast.makeText(requireContext(), state.content, Toast.LENGTH_LONG).show()
                        viewModel.resetState()
                    }

                    is LoginUiState.ValidationFailed -> {
                        binding.loadingOverlay.visibility = View.GONE
                        binding.textInputLayoutEmail.error = state.validation.emailError
                        binding.textInputLayoutPassword.error = state.validation.passwordError
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showForgotPasswordDialog() {
        val context = requireContext()
        val inputLayout = TextInputLayout(context).apply {
            hint = "Email"
            setPadding(48, 24, 48, 24)
        }
        val editText = TextInputEditText(context).apply {
            inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }
        inputLayout.addView(editText)

        MaterialAlertDialogBuilder(context)
            .setTitle("Восстановление пароля")
            .setView(inputLayout)
            .setPositiveButton("Отправить") { _, _ ->
                val email = editText.text?.toString()?.trim() ?: ""
                if (email.isNotBlank()) {
                    viewModel.sendResetRequest(email)
                } else {
                    Toast.makeText(context, "Введите email", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}