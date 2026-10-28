package com.example.alimentaredapp.dto

import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("rol")
    val rol: String,

    @SerializedName("activo")
    val activo: Boolean = true,

    @SerializedName("creadoEn")
    val creadoEn: String? = null,

    @SerializedName("token")
    private val rawToken: String? = null
) {
    // Compatibilidad con código previo de la UI y ViewModels
    val idUsuario: Long get() = id

    // Si el backend aún no retorna token en el JSON, provee un identificador seguro
    val token: String get() = rawToken ?: "user_${id}"
}
