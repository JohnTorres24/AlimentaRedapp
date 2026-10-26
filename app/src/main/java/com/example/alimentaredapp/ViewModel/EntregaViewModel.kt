package com.example.alimentaredapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alimentaredapp.dto.ConfirmarEntregaRequestDto
import com.example.alimentaredapp.Repository.EntregaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EntregaViewModel : ViewModel() {
    private val repository = EntregaRepository()

    private val _uiState = MutableStateFlow<EntregaUiState>(EntregaUiState.Idle)
    val uiState: StateFlow<EntregaUiState> = _uiState

    fun confirmarEntrega(token: String, idEntrega: Long, responsable: String, notas: String?) {
        viewModelScope.launch {
            _uiState.value = EntregaUiState.Loading
            val dto = ConfirmarEntregaRequestDto(responsable, notas)
            val result = repository.confirmarEntrega(token, idEntrega, dto)

            result.fold(
                onSuccess = { response ->
                    _uiState.value = EntregaUiState.Success(response)
                },
                onFailure = { exception ->
                    _uiState.value = EntregaUiState.Error(exception.message ?: "Error al confirmar entrega")
                }
            )
        }
    }
}