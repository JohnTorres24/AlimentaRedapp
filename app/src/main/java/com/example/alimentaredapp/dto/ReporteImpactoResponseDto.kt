package com.example.alimentaredapp.dto

import com.google.gson.annotations.SerializedName

data class ReporteImpactoResponseDto(
    @SerializedName("totalLotesPublicados")
    val totalLotesPublicados: Long = 0L,

    @SerializedName("totalSolicitudesRealizadas")
    val totalSolicitudesRealizadas: Long = 0L,

    @SerializedName("totalOrganizacionesRegistradas")
    val totalOrganizacionesRegistradas: Long = 0L
)
