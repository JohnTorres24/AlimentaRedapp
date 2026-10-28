package com.example.alimentaredapp.dto

import com.google.gson.annotations.SerializedName

data class LoteResponseDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("descripcion")
    val descripcion: String? = null,

    @SerializedName("cantidadTotal")
    val cantidadTotal: Double,

    @SerializedName("cantidadDisponible")
    val cantidadDisponible: Double,

    @SerializedName("unidadMedida")
    val unidadMedida: String,

    @SerializedName("fechaExpiracion")
    val fechaExpiracion: String,

    @SerializedName("estado")
    val estado: String,

    @SerializedName("nombreOrganizacion")
    val nombreOrganizacion: String? = null,

    @SerializedName("nombreCategoria")
    val nombreCategoria: String? = null
)
