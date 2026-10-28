package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.Exception.ApiException
import com.example.alimentaredapp.dto.ReporteImpactoResponseDto

class ReporteRepository {
    private val api = RetrofitClient.instance.create(ReporteApi::class.java)

    suspend fun obtenerImpacto(token: String): Result<ReporteImpactoResponseDto> {
        return try {
            val authHeader = if (token.isNotBlank()) "Bearer $token" else null
            val response = api.obtenerImpacto(authHeader)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(ApiException("Error al obtener indicadores: Código ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(ApiException("Error de conexión al obtener indicadores: ${e.message}"))
        }
    }
}
