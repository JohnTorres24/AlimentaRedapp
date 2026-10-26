package com.example.alimentaredapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alimentaredapp.dto.SolicitudRequestDto
import com.example.alimentaredapp.Repository.SolicitudRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SolicitudViewModel : ViewModel() {
    private val repository = SolicitudRepository()

    private val _uiState = MutableStateFlow<SolicitudUiState>(SolicitudUiState.Idle)
    val uiState: StateFlow<SolicitudUiState> = _uiState

    fun solicitarReserva(token: String, loteId: Long, cantidad: Double, observaciones: String) {
        viewModelScope.launch {
            _uiState.value = SolicitudUiState.Loading

            val dto = SolicitudRequestDto(
                loteId = loteId,
                cantidad = cantidad,
                observaciones = observaciones
            )

            val result = repository.solicitarReserva(token, dto)

            result.fold(
                onSuccess = { response ->
                    _uiState.value = SolicitudUiState.Success("Reserva completada. Estado: ${response.estado}")
                },
                onFailure = { exception ->
                    _uiState.value = SolicitudUiState.Error(exception.message ?: "Error desconocido")
                }
            )
        }
    }
}