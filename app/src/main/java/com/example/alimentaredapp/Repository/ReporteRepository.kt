package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.Exception.ApiException
import com.example.alimentaredapp.dto.ReporteImpactoResponseDto
import com.example.alimentaredapp.util.ErrorHelper

class ReporteRepository {
    private val api = RetrofitClient.instance.create(ReporteApi::class.java)

    suspend fun obtenerImpacto(token: String): Result<ReporteImpactoResponseDto> {
        return try {
            val authHeader = if (token.isNotBlank()) "Bearer $token" else null
            val response = api.obtenerImpacto(authHeader)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val raw = response.errorBody()?.string()
                val mensaje = ErrorHelper.mapHttpError(response.code(), raw, "obtener los indicadores")
                Result.failure(ApiException(mensaje))
            }
        } catch (e: Exception) {
            val mensaje = ErrorHelper.mapNetworkException(e, "obtener los indicadores")
            Result.failure(ApiException(mensaje))
        }
    }
}
