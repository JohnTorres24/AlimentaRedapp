package com.example.alimentaredapp.dto

import com.google.gson.annotations.SerializedName

data class PublicarLoteRequestDto(
    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("descripcion")
    val descripcion: String? = null,

    @SerializedName("cantidadTotal")
    val cantidadTotal: Double,

    @SerializedName("unidadMedida")
    val unidadMedida: String,

    @SerializedName("fechaExpiracion")
    val fechaExpiracion: String, // Formato esperado por backend: "yyyy-MM-dd'T'HH:mm:ss"

    @SerializedName("organizacionId")
    val organizacionId: Long = 1L,

    @SerializedName("categoriaId")
    val categoriaId: Long = 1L
)
