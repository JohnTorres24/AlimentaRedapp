package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.ConfirmarEntregaRequestDto
import com.example.alimentaredapp.dto.ConfirmarEntregaResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface EntregaApi {
    @POST("entregas/{id}/confirmar")
    suspend fun confirmarEntrega(
        @Header("Authorization") token: String,
        @Path("id") idEntrega: Long,
        @Body request: ConfirmarEntregaRequestDto
    ): Response<ConfirmarEntregaResponseDto>
}