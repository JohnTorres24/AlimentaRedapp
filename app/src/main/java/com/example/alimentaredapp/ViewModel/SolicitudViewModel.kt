package com.example.alimentaredapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alimentaredapp.dto.SolicitudRequestDto
import com.example.alimentaredapp.Repository.SolicitudRepository
import com.example.alimentaredapp.util.SessionManager
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

            // Se obtiene el ID del usuario en sesión o se utiliza un receptor válido en Render
            val usuarioIdSeguro = when {
                SessionManager.userId > 0 -> SessionManager.userId
                else -> 4L // Usuario receptor registrado en Render ("Fundacion Alimentos Cajica")
            }

            val dto = SolicitudRequestDto(
                loteId = loteId,
                usuarioId = usuarioIdSeguro,
                cantidadSolicitada = cantidad,
                observaciones = observaciones
            )

            val result = repository.solicitarReserva(token, dto)

            result.fold(
                onSuccess = { response ->
                    _uiState.value = SolicitudUiState.Success("¡Reserva solicitada exitosamente! Estado: ${response.estado}")
                },
                onFailure = { exception ->
                    _uiState.value = SolicitudUiState.Error(exception.message ?: "Ocurrió un error al procesar tu solicitud")
                }
            )
        }
    }
}
