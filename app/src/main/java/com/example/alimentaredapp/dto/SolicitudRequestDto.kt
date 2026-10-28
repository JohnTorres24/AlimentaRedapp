package com.example.alimentaredapp.dto

import com.google.gson.annotations.SerializedName

data class SolicitudRequestDto(
    @SerializedName("loteId")
    val loteId: Long,

    @SerializedName("usuarioId")
    val usuarioId: Long = 4L,

    @SerializedName("cantidadSolicitada")
    val cantidadSolicitada: Double,

    // Campo local de notas u observaciones para seguimiento
    @Transient
    val observaciones: String? = null
) {
    // Constructor de conveniencia
    constructor(loteId: Long, cantidad: Double, observaciones: String? = null) : this(
        loteId = loteId,
        usuarioId = 4L,
        cantidadSolicitada = cantidad,
        observaciones = observaciones
    )

    val cantidad: Double get() = cantidadSolicitada
}
