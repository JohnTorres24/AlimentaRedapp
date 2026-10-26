package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.LoteResponseDto
import com.example.alimentaredapp.dto.PublicarLoteRequestDto
import com.example.alimentaredapp.dto.PublicarLoteResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface LoteApi {
    @GET("lotes/disponibles")
    suspend fun obtenerLotesDisponibles(
        @Header("Authorization") token: String,
        @Query("categoria") categoria: String? = null // Permite filtrar por categoría si se requiere
    ): Response<List<LoteResponseDto>>
    @POST("lotes")
    suspend fun publicarLote(
        @Header("Authorization") token: String,
        @Body request: PublicarLoteRequestDto
    ): Response<PublicarLoteResponseDto>
}