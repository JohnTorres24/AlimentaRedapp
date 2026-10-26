package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.ConfirmarEntregaRequestDto
import com.example.alimentaredapp.dto.ConfirmarEntregaResponseDto
import com.example.alimentaredapp.Exception.ApiException

class EntregaRepository {
    private val api = RetrofitClient.instance.create(EntregaApi::class.java)

    suspend fun confirmarEntrega(
        token: String,
        idEntrega: Long,
        dto: ConfirmarEntregaRequestDto
    ): Result<ConfirmarEntregaResponseDto> {
        return try {
            val response = api.confirmarEntrega("Bearer $token", idEntrega, dto)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(ApiException("Error al confirmar la entrega: Código ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(ApiException("Error de red al confirmar entrega: ${e.message}"))
        }
    }
}