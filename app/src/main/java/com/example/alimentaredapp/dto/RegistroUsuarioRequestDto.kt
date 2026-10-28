package com.example.alimentaredapp.dto

import com.google.gson.annotations.SerializedName

data class RegistroUsuarioRequestDto(
    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("rol")
    val rol: String // "ADMIN", "DONANTE", "ORGANIZACION"
)
