package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.SolicitudRequestDto
import com.example.alimentaredapp.dto.SolicitudResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface SolicitudApi {
    @POST("solicitudes")
    suspend fun solicitarReserva(
        @Header("Authorization") token: String? = null,
        @Body request: SolicitudRequestDto
    ): Response<SolicitudResponseDto>

    @GET("solicitudes/usuario/{usuarioId}")
    suspend fun listarPorUsuario(
        @Header("Authorization") token: String? = null,
        @Path("usuarioId") usuarioId: Long
    ): Response<List<SolicitudResponseDto>>

    @GET("solicitudes/lote/{loteId}")
    suspend fun listarPorLote(
        @Header("Authorization") token: String? = null,
        @Path("loteId") loteId: Long
    ): Response<List<SolicitudResponseDto>>

    @PUT("solicitudes/{id}/estado")
    suspend fun cambiarEstado(
        @Header("Authorization") token: String? = null,
        @Path("id") id: Long,
        @Query("estado") estado: String
    ): Response<SolicitudResponseDto>
}
