package com.example.alimentaredapp.dto

import com.google.gson.annotations.SerializedName

data class RegistroOrganizacionResponseDto(
    @SerializedName("id")
    val id: Long = 0L,

    @SerializedName("nombre")
    val nombre: String = "",

    @SerializedName("nitIdentificacion")
    val nitIdentificacion: String = "",

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
    val usuarioId: Long? = null
) {
    // Compatibilidad con código previo de la UI y ViewModels
    val idOrganizacion: Long get() = id
    val idUsuario: Long get() = usuarioId ?: 0L
    val mensaje: String get() = if (nombre.isNotBlank()) "Organización '$nombre' registrada con éxito" else "Organización registrada con éxito"
}
