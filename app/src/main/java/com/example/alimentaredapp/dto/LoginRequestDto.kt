package com.example.alimentaredapp.dto

data class LoginRequestDto(
    val email: String,
    val passwordHash: String // O password, dependiendo de cómo lo espere tu backend
)