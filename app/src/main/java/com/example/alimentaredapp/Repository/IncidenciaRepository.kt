package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.ReportarIncidenciaRequestDto
import com.example.alimentaredapp.dto.ReportarIncidenciaResponseDto
import com.example.alimentaredapp.Exception.ApiException
import com.example.alimentaredapp.util.ErrorHelper

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
                val raw = response.errorBody()?.string()
                val mensaje = ErrorHelper.mapHttpError(response.code(), raw, "reportar la incidencia")
                Result.failure(ApiException(mensaje))
            }
        } catch (e: Exception) {
            val mensaje = ErrorHelper.mapNetworkException(e, "reportar la incidencia")
            Result.failure(ApiException(mensaje))
        }
    }
}
