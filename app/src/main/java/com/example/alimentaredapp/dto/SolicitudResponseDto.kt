package com.example.alimentaredapp.dto

import com.google.gson.annotations.SerializedName

data class SolicitudResponseDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("loteId")
    val loteId: Long? = null,

    @SerializedName("tituloLote")
    val tituloLote: String? = null,

    @SerializedName("usuarioId")
    val usuarioId: Long? = null,

    @SerializedName("nombreUsuario")
    val nombreUsuario: String? = null,

    @SerializedName("cantidadSolicitada")
    val cantidadSolicitada: Double? = null,

    @SerializedName("estado")
    val estado: String,

    @SerializedName("fechaSolicitud")
    val fechaSolicitud: String? = null
) {
    // Compatibilidad con código previo de la UI
    val mensaje: String get() = "Solicitud #$id ($estado)"
}
