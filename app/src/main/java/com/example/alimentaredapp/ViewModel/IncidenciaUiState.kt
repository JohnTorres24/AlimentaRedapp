package com.example.alimentaredapp.ViewModel

import com.example.alimentaredapp.dto.ReportarIncidenciaResponseDto

sealed class IncidenciaUiState {
    object Idle : IncidenciaUiState()
    object Loading : IncidenciaUiState()
    data class Success(val data: ReportarIncidenciaResponseDto) : IncidenciaUiState()
    data class Error(val message: String) : IncidenciaUiState()
}