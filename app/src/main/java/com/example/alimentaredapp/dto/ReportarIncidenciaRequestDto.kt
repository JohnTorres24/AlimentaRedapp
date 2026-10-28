package com.example.alimentaredapp.dto

import com.google.gson.annotations.SerializedName

data class ReportarIncidenciaRequestDto(
    @SerializedName("motivo")
    val motivo: String,

    @SerializedName("descripcion")
    val descripcion: String,

    @SerializedName("entregaId")
    val entregaId: Long? = null,

    @SerializedName("usuarioId")
    val usuarioId: Long? = null
)
