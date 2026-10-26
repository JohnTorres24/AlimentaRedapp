package com.example.alimentaredapp.ViewModel

import com.example.alimentaredapp.dto.LoginResponseDto

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val data: LoginResponseDto) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}