package com.example.alimentaredapp.dto

import com.google.gson.annotations.SerializedName

data class PublicarLoteResponseDto(
    @SerializedName("id")
    val id: Long = 0L,

    @SerializedName("titulo")
    val titulo: String = "",

    @SerializedName("descripcion")
    val descripcion: String? = null,

    @SerializedName("cantidadTotal")
    val cantidadTotal: Double = 0.0,

    @SerializedName("cantidadDisponible")
    val cantidadDisponible: Double = 0.0,

    @SerializedName("unidadMedida")
    val unidadMedida: String = "",

    @SerializedName("fechaExpiracion")
    val fechaExpiracion: String? = null,

    @SerializedName("estado")
    val estado: String = "DISPONIBLE",

    @SerializedName("nombreOrganizacion")
    val nombreOrganizacion: String? = null,

    @SerializedName("nombreCategoria")
    val nombreCategoria: String? = null
) {
    // Compatibilidad con código previo de la UI (PublicarLoteScreen y LoteViewModel)
    val idLote: Long get() = id
    val mensaje: String get() = if (titulo.isNotBlank()) "Lote '$titulo' publicado con éxito (Estado: $estado)" else "Lote publicado con éxito"
}
