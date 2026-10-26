package com.example.alimentaredapp.ViewModel

import com.example.alimentaredapp.dto.ConfirmarEntregaResponseDto

sealed class EntregaUiState {
    object Idle : EntregaUiState()
    object Loading : EntregaUiState()
    data class Success(val data: ConfirmarEntregaResponseDto) : EntregaUiState()
    data class Error(val message: String) : EntregaUiState()
}