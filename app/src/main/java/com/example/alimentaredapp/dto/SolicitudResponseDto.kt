package com.example.alimentaredapp.dto

data class SolicitudResponseDto(
    val id: Long,
    val estado: String,
    val mensaje: String? = null
)
