package com.example.alimentaredapp.dto

import com.google.gson.annotations.SerializedName

data class ConfirmarEntregaRequestDto(
    @SerializedName("responsable")
    val responsable: String? = null,

    @SerializedName("notas")
    val notas: String? = null
)
