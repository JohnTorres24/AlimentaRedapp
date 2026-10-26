package com.example.alimentaredapp.dto

data class SolicitudRequestDto(
    val loteId: Long,
    val cantidad: Double,
    val observaciones: String? = null
)