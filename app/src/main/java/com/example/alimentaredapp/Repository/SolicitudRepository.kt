package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.SolicitudRequestDto
import com.example.alimentaredapp.dto.SolicitudResponseDto
import com.example.alimentaredapp.Exception.ApiException

class SolicitudRepository {
    private val api = RetrofitClient.instance.create(SolicitudApi::class.java)

    suspend fun solicitarReserva(token: String, dto: SolicitudRequestDto): Result<SolicitudResponseDto> {
        return try {
            val response = api.solicitarReserva("Bearer $token", dto)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                // Lanza tu excepción personalizada si hay error 400/500
                Result.failure(ApiException("Error en la reserva: Código ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(ApiException("Error de conexión: ${e.message}"))
        }
    }
}