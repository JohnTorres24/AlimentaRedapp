package com.example.alimentaredapp.ViewModel

import com.example.alimentaredapp.dto.LoteResponseDto

sealed class LoteUiState {
    object Idle : LoteUiState()
    object Loading : LoteUiState()
    data class SuccessConsultar(val lotes: List<LoteResponseDto>) : LoteUiState()
    data class SuccessPublicar(val mensaje: String) : LoteUiState()
    data class Error(val message: String) : LoteUiState()
}