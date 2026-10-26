package com.example.alimentaredapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alimentaredapp.dto.LoginRequestDto
import com.example.alimentaredapp.Repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    fun login(email: String, passwordHash: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = repository.login(LoginRequestDto(email, passwordHash))

            result.fold(
                onSuccess = { response ->
                    _uiState.value = AuthUiState.Success(response)
                },
                onFailure = { exception ->
                    _uiState.value = AuthUiState.Error(exception.message ?: "Error de autenticación")
                }
            )
        }
    }
}