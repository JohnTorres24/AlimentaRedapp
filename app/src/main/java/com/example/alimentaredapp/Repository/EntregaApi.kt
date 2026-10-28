package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.ConfirmarEntregaResponseDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface EntregaApi {
    // Confirmación de recepción de entrega (PUT /api/entregas/{id}/confirmar)
    @PUT("entregas/{id}/confirmar")
    suspend fun confirmarEntrega(
        @Header("Authorization") token: String? = null,
        @Path("id") idEntrega: Long
    ): Response<ResponseBody>

    // Programación de una nueva entrega (POST /api/entregas/programar)
    @POST("entregas/programar")
    suspend fun programarEntrega(
        @Header("Authorization") token: String? = null,
        @Query("solicitudId") solicitudId: Long,
        @Query("responsableRetiro") responsableRetiro: String
    ): Response<ConfirmarEntregaResponseDto>
}
