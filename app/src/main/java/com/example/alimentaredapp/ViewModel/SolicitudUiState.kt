package com.example.alimentaredapp.ViewModel

sealed class SolicitudUiState {
    object Idle : SolicitudUiState()
    object Loading : SolicitudUiState()
    data class Success(val mensaje: String) : SolicitudUiState()
    data class Error(val error: String) : SolicitudUiState()
}