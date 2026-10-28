package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.ReportarIncidenciaRequestDto
import com.example.alimentaredapp.dto.ReportarIncidenciaResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface IncidenciaApi {
    @POST("entregas/{idEntrega}/incidencias")
    suspend fun reportarIncidencia(
        @Header("Authorization") token: String? = null,
        @Path("idEntrega") idEntrega: Long,
        @Body request: ReportarIncidenciaRequestDto
    ): Response<ReportarIncidenciaResponseDto>
}
