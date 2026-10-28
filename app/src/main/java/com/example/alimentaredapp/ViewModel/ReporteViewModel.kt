package com.example.alimentaredapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alimentaredapp.Repository.ReporteRepository
import com.example.alimentaredapp.dto.ReporteImpactoResponseDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ReporteUiState {
    object Idle : ReporteUiState()
    object Loading : ReporteUiState()
    data class Success(val data: ReporteImpactoResponseDto) : ReporteUiState()
    data class Error(val message: String) : ReporteUiState()
}

class ReporteViewModel : ViewModel() {
    private val repository = ReporteRepository()

    private val _uiState = MutableStateFlow<ReporteUiState>(ReporteUiState.Idle)
    val uiState: StateFlow<ReporteUiState> = _uiState

    fun cargarIndicadores(token: String) {
        viewModelScope.launch {
            _uiState.value = ReporteUiState.Loading
            val result = repository.obtenerImpacto(token)

            result.fold(
                onSuccess = { response ->
                    _uiState.value = ReporteUiState.Success(response)
                },
                onFailure = { exception ->
                    _uiState.value = ReporteUiState.Error(exception.message ?: "Error al cargar indicadores")
                }
            )
        }
    }
}
