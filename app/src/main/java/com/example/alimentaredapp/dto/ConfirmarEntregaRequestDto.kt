package com.example.alimentaredapp.dto

data class ConfirmarEntregaRequestDto(
    val responsable: String,
    val notas: String? = null
)