package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.LoteResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface LoteApi {
    @GET("lotes/disponibles")
    suspend fun obtenerLotesDisponibles(
        @Header("Authorization") token: String,
        @Query("categoria") categoria: String? = null // Permite filtrar por categoría si se requiere
    ): Response<List<LoteResponseDto>>
}