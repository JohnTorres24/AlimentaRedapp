package com.example.alimentaredapp.ViewModel

import com.example.alimentaredapp.dto.RegistroOrganizacionResponseDto

sealed class OrganizacionUiState {
    object Idle : OrganizacionUiState()
    object Loading : OrganizacionUiState()
    data class Success(val data: RegistroOrganizacionResponseDto) : OrganizacionUiState()
    data class Error(val message: String) : OrganizacionUiState()
}