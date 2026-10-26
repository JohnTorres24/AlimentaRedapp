package com.example.alimentaredapp.dto

data class PublicarLoteRequestDto(
    val titulo: String,
    val descripcion: String,
    val cantidadTotal: Double,
    val unidadMedida: String,
    val fechaExpiracion: String, // Formato "YYYY-MM-DDTHH:mm:ss"
    val categoriaId: Long
)