package com.example.alimentaredapp.dto

data class LoteResponseDto(
    val id: Long,
    val titulo: String,
    val descripcion: String,
    val cantidadTotal: Double,
    val cantidadDisponible: Double,
    val unidadMedida: String,
    val fechaExpiracion: String, // Usamos String para facilitar el parseo del JSON
    val estado: String
)