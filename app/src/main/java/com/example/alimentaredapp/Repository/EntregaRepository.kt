package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.ConfirmarEntregaRequestDto
import com.example.alimentaredapp.dto.ConfirmarEntregaResponseDto
import com.example.alimentaredapp.Exception.ApiException

class EntregaRepository {
    private val api = RetrofitClient.instance.create(EntregaApi::class.java)

    suspend fun confirmarEntrega(
        token: String,
        idEntrega: Long,
        dto: ConfirmarEntregaRequestDto? = null
    ): Result<ConfirmarEntregaResponseDto> {
        return try {
            val response = api.confirmarEntrega("Bearer $token", idEntrega)

            if (response.isSuccessful) {
                val textoRespuesta = response.body()?.string() ?: "Entrega confirmada exitosamente"
                Result.success(
                    ConfirmarEntregaResponseDto(
                        id = idEntrega,
                        estado = "ENTREGADA",
                        mensaje = textoRespuesta
                    )
                )
            } else {
                Result.failure(ApiException("Error al confirmar la entrega: Código ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(ApiException("Error de red al confirmar entrega: ${e.message}"))
        }
    }
}
