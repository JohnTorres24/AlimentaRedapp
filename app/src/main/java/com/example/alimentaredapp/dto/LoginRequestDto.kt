package com.example.alimentaredapp.dto

import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
) {
    // Compatibilidad para código existente
    val passwordHash: String get() = password
}
