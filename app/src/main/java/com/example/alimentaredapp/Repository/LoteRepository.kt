package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.LoteResponseDto
import com.example.alimentaredapp.dto.PublicarLoteRequestDto
import com.example.alimentaredapp.dto.PublicarLoteResponseDto
import com.example.alimentaredapp.Exception.ApiException
import com.example.alimentaredapp.util.ErrorHelper

class LoteRepository {
    private val api = RetrofitClient.instance.create(LoteApi::class.java)

    suspend fun obtenerLotesDisponibles(token: String, categoria: String? = null): Result<List<LoteResponseDto>> {
        return try {
            val response = api.obtenerLotesDisponibles("Bearer $token", categoria)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val raw = response.errorBody()?.string()
                val mensaje = ErrorHelper.mapHttpError(response.code(), raw, "consultar los lotes disponibles")
                Result.failure(ApiException(mensaje))
            }
        } catch (e: Exception) {
            val mensaje = ErrorHelper.mapNetworkException(e, "consultar los lotes")
            Result.failure(ApiException(mensaje))
        }
    }

    suspend fun obtenerLotePorId(token: String, id: Long): Result<LoteResponseDto> {
        return try {
            val response = api.obtenerLotePorId("Bearer $token", id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val raw = response.errorBody()?.string()
                val mensaje = ErrorHelper.mapHttpError(response.code(), raw, "consultar el detalle del lote")
                Result.failure(ApiException(mensaje))
            }
        } catch (e: Exception) {
            val mensaje = ErrorHelper.mapNetworkException(e, "obtener la información del lote")
            Result.failure(ApiException(mensaje))
        }
    }

    suspend fun obtenerCategorias(token: String? = null): Result<List<CategoriaResponseDto>> {
        return try {
            val authHeader = if (!token.isNullOrBlank()) "Bearer $token" else null
            val response = api.obtenerCategorias(authHeader)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val raw = response.errorBody()?.string()
                val mensaje = ErrorHelper.mapHttpError(response.code(), raw, "consultar las categorías de alimentos")
                Result.failure(ApiException(mensaje))
            }
        } catch (e: Exception) {
            val mensaje = ErrorHelper.mapNetworkException(e, "cargar las categorías")
            Result.failure(ApiException(mensaje))
        }
    }

    suspend fun publicarLote(token: String, dto: PublicarLoteRequestDto): Result<PublicarLoteResponseDto> {
        return try {
            val response = api.publicarLote("Bearer $token", dto)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val raw = response.errorBody()?.string()
                val mensaje = ErrorHelper.mapHttpError(response.code(), raw, "publicar el lote de alimentos")
                Result.failure(ApiException(mensaje))
            }
        } catch (e: Exception) {
            val mensaje = ErrorHelper.mapNetworkException(e, "publicar el lote")
            Result.failure(ApiException(mensaje))
        }
    }
}
