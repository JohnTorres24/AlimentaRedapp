package com.example.alimentaredapp.dto

import com.google.gson.annotations.SerializedName

data class ReportarIncidenciaResponseDto(
    @SerializedName("id")
    val id: Long = 0L,

    @SerializedName("motivo")
    val motivo: String? = null,

    @SerializedName("descripcion")
    val descripcion: String? = null,

    @SerializedName("resuelta")
    val resuelta: Boolean = false,

    @SerializedName("fechaReporte")
    val fechaReporte: String? = null,

    @SerializedName("mensaje")
    val mensaje: String = "Incidencia reportada exitosamente"
) {
    // Compatibilidad con código previo de la UI
    val idIncidencia: Long get() = id
}
