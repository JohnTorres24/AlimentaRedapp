package com.example.alimentaredapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alimentaredapp.dto.PublicarLoteRequestDto
import com.example.alimentaredapp.Repository.LoteRepository
import com.example.alimentaredapp.util.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoteViewModel : ViewModel() {
    private val repository = LoteRepository()

    private val _uiState = MutableStateFlow<LoteUiState>(LoteUiState.Idle)
    val uiState: StateFlow<LoteUiState> = _uiState

    fun obtenerLotes(token: String, categoria: String? = null) {
        viewModelScope.launch {
            _uiState.value = LoteUiState.Loading
            val tokenFinal = if (token.isNotBlank()) token else SessionManager.token
            val result = repository.obtenerLotesDisponibles(tokenFinal, categoria)

            result.fold(
                onSuccess = { lotes ->
                    _uiState.value = LoteUiState.SuccessConsultar(lotes)
                },
                onFailure = { exception ->
                    _uiState.value = LoteUiState.Error(exception.message ?: "Error al cargar lotes")
                }
            )
        }
    }

    fun publicarLote(token: String, dto: PublicarLoteRequestDto) {
        viewModelScope.launch {
            _uiState.value = LoteUiState.Loading
            val tokenFinal = if (token.isNotBlank()) token else SessionManager.token
            val result = repository.publicarLote(tokenFinal, dto)

            result.fold(
                onSuccess = { response ->
                    _uiState.value = LoteUiState.SuccessPublicar(response.mensaje)
                },
                onFailure = { exception ->
                    _uiState.value = LoteUiState.Error(exception.message ?: "Error al publicar lote")
                }
            )
        }
    }
}
