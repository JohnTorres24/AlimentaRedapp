package com.example.alimentaredapp.dto

import com.google.gson.annotations.SerializedName

data class ConfirmarEntregaResponseDto(
    @SerializedName("id")
    val id: Long = 0L,

    @SerializedName("estado")
    val estado: String = "ENTREGADA",

    @SerializedName("mensaje")
    val mensaje: String = "Entrega confirmada exitosamente",

    @SerializedName("fechaRecepcion")
    val fechaRecepcion: String? = null,

    @SerializedName("responsableRetiro")
    val responsableRetiro: String? = null,

    @SerializedName("responsableRecepcion")
    val responsableRecepcion: String? = null
) {
    // Compatibilidad con código previo de la UI
    val idEntrega: Long get() = id
}
