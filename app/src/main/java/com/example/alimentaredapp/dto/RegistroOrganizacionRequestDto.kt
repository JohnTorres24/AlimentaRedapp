package com.example.alimentaredapp.dto

import com.google.gson.annotations.SerializedName

data class RegistroOrganizacionRequestDto(
    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("nitIdentificacion")
    val nitIdentificacion: String,

    @SerializedName("direccion")
    val direccion: String? = null,

    @SerializedName("latitud")
    val latitud: Double? = null,

    @SerializedName("longitud")
    val longitud: Double? = null,

    @SerializedName("tipoOrganizacion")
    val tipoOrganizacion: String? = null,

    @SerializedName("esDonante")
    val esDonante: Boolean = false,

    @SerializedName("esReceptora")
    val esReceptora: Boolean = false,

    @SerializedName("usuarioId")
    val usuarioId: Long = 1L,

    // Campos locales opcionales usados en el formulario
    val email: String? = null,
    val passwordHash: String? = null
) {
    // Constructor de conveniencia compatible con RegistroOrganizacionScreen
    constructor(
        nombre: String,
        email: String,
        passwordHash: String,
        nitIdentificacion: String,
        direccion: String,
        latitud: Double,
        longitud: Double,
        tipoOrganizacion: String
    ) : this(
        nombre = nombre,
        nitIdentificacion = nitIdentificacion,
        direccion = direccion,
        latitud = latitud,
        longitud = longitud,
        tipoOrganizacion = tipoOrganizacion,
        esDonante = tipoOrganizacion.equals("DONANTE", ignoreCase = true),
        esReceptora = tipoOrganizacion.equals("RECEPTOR", ignoreCase = true) || tipoOrganizacion.equals("ORGANIZACION", ignoreCase = true),
        usuarioId = 1L,
        email = email,
        passwordHash = passwordHash
    )
}
