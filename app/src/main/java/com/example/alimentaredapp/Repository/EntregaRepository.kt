package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.ConfirmarEntregaRequestDto
import com.example.alimentaredapp.dto.ConfirmarEntregaResponseDto
import com.example.alimentaredapp.Exception.ApiException
import com.example.alimentaredapp.util.ErrorHelper

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
                val raw = response.errorBody()?.string()
                val mensaje = ErrorHelper.mapHttpError(response.code(), raw, "confirmar la entrega")
                Result.failure(ApiException(mensaje))
            }
        } catch (e: Exception) {
            val mensaje = ErrorHelper.mapNetworkException(e, "confirmar la entrega")
            Result.failure(ApiException(mensaje))
        }
    }
}
