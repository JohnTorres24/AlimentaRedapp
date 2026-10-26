package com.example.alimentaredapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alimentaredapp.dto.RegistroOrganizacionRequestDto
import com.example.alimentaredapp.Repository.OrganizacionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrganizacionViewModel : ViewModel() {
    private val repository = OrganizacionRepository()

    private val _uiState = MutableStateFlow<OrganizacionUiState>(OrganizacionUiState.Idle)
    val uiState: StateFlow<OrganizacionUiState> = _uiState

    fun registrarOrganizacion(dto: RegistroOrganizacionRequestDto) {
        viewModelScope.launch {
            _uiState.value = OrganizacionUiState.Loading
            val result = repository.registrarOrganizacion(dto)

            result.fold(
                onSuccess = { response ->
                    _uiState.value = OrganizacionUiState.Success(response)
                },
                onFailure = { exception ->
                    _uiState.value = OrganizacionUiState.Error(exception.message ?: "Error al registrar organización")
                }
            )
        }
    }
}