package com.example.alimentaredapp.dto

data class RegistroOrganizacionRequestDto(
    val nombre: String,
    val email: String,
    val passwordHash: String,
    val nitIdentificacion: String,
    val direccion: String,
    val latitud: Double, // Para el mapa/geolocalización
    val longitud: Double,
    val tipoOrganizacion: String // Ejemplo: "DONANTE" o "RECEPTOR"
)