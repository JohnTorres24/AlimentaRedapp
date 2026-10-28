package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.SolicitudRequestDto
import com.example.alimentaredapp.dto.SolicitudResponseDto
import com.example.alimentaredapp.Exception.ApiException
import com.example.alimentaredapp.util.ErrorHelper

class SolicitudRepository {
    private val api = RetrofitClient.instance.create(SolicitudApi::class.java)

    suspend fun solicitarReserva(token: String, dto: SolicitudRequestDto): Result<SolicitudResponseDto> {
        return try {
            val response = api.solicitarReserva("Bearer $token", dto)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val raw = response.errorBody()?.string()
                val mensaje = ErrorHelper.mapHttpError(response.code(), raw, "reservar el lote de alimentos")
                Result.failure(ApiException(mensaje))
            }
        } catch (e: Exception) {
            val mensaje = ErrorHelper.mapNetworkException(e, "procesar la reserva")
            Result.failure(ApiException(mensaje))
        }
    }
}
