package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.ReporteImpactoResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ReporteApi {
    @GET("reportes/impacto")
    suspend fun obtenerImpacto(
        @Header("Authorization") token: String? = null
    ): Response<ReporteImpactoResponseDto>
}
