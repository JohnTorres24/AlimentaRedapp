package com.example.alimentaredapp.dto

data class ReportarIncidenciaResponseDto(
    val idIncidencia: Long,
    val resuelta: Boolean,
    val mensaje: String
)