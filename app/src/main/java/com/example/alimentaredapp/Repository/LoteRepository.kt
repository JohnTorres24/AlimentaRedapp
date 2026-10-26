package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.LoteResponseDto
import com.example.alimentaredapp.Exception.ApiException

class LoteRepository {
    private val api = RetrofitClient.instance.create(LoteApi::class.java)

    suspend fun obtenerLotesDisponibles(token: String, categoria: String? = null): Result<List<LoteResponseDto>> {
        return try {
            val response = api.obtenerLotesDisponibles("Bearer $token", categoria)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(ApiException("Error al obtener los lotes: Código ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(ApiException("Error de red al consultar lotes: ${e.message}"))
        }
    }
}