package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.ReportarIncidenciaRequestDto
import com.example.alimentaredapp.dto.ReportarIncidenciaResponseDto
import com.example.alimentaredapp.Exception.ApiException

class IncidenciaRepository {
    private val api = RetrofitClient.instance.create(IncidenciaApi::class.java)

    suspend fun reportarIncidencia(
        token: String,
        idEntrega: Long,
        dto: ReportarIncidenciaRequestDto
    ): Result<ReportarIncidenciaResponseDto> {
        return try {
            val response = api.reportarIncidencia("Bearer $token", idEntrega, dto)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(ApiException("Error al reportar incidencia: Código ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(ApiException("Error de red al reportar: ${e.message}"))
        }
    }
}