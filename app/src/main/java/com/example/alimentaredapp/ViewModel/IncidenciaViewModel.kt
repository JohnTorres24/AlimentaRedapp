package com.example.alimentaredapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alimentaredapp.dto.ReportarIncidenciaRequestDto
import com.example.alimentaredapp.Repository.IncidenciaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class IncidenciaViewModel : ViewModel() {
    private val repository = IncidenciaRepository()

    private val _uiState = MutableStateFlow<IncidenciaUiState>(IncidenciaUiState.Idle)
    val uiState: StateFlow<IncidenciaUiState> = _uiState

    fun reportarIncidencia(token: String, idEntrega: Long, motivo: String, descripcion: String) {
        viewModelScope.launch {
            _uiState.value = IncidenciaUiState.Loading
            val dto = ReportarIncidenciaRequestDto(motivo, descripcion)
            val result = repository.reportarIncidencia(token, idEntrega, dto)

            result.fold(
                onSuccess = { response ->
                    _uiState.value = IncidenciaUiState.Success(response)
                },
                onFailure = { exception ->
                    _uiState.value = IncidenciaUiState.Error(exception.message ?: "Error al reportar incidencia")
                }
            )
        }
    }
}