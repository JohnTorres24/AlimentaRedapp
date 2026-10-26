package com.example.alimentaredapp.dto

data class LoginResponseDto(
    val token: String,
    val idUsuario: Long,
    val nombre: String,
    val rol: String
)