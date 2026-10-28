package com.example.alimentaredapp.dto

import com.google.gson.annotations.SerializedName

data class SolicitudRequestDto(
    @SerializedName("loteId")
    val loteId: Long,

    @SerializedName("usuarioId")
    val usuarioId: Long = 1L,

    @SerializedName("cantidadSolicitada")
    val cantidadSolicitada: Double,

    // Campo local de notas u observaciones (no requerido por backend)
    @Transient
    val observaciones: String? = null
) {
    // Constructor de compatibilidad con llamadas existentes
    constructor(loteId: Long, cantidad: Double, observaciones: String? = null) : this(
        loteId = loteId,
        usuarioId = 1L,
        cantidadSolicitada = cantidad,
        observaciones = observaciones
    )

    // Propiedad de conveniencia
    val cantidad: Double get() = cantidadSolicitada
}
