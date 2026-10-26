package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.SolicitudRequestDto
import com.example.alimentaredapp.dto.SolicitudResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface SolicitudApi {
    @POST("solicitudes")
    suspend fun solicitarReserva(
        @Header("Authorization") token: String,
        @Body request: SolicitudRequestDto
    ): Response<SolicitudResponseDto>
}