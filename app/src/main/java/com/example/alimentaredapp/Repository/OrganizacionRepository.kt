package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.RegistroOrganizacionRequestDto
import com.example.alimentaredapp.dto.RegistroOrganizacionResponseDto
import com.example.alimentaredapp.Exception.ApiException

class OrganizacionRepository {
    private val api = RetrofitClient.instance.create(OrganizacionApi::class.java)

    suspend fun registrarOrganizacion(dto: RegistroOrganizacionRequestDto): Result<RegistroOrganizacionResponseDto> {
        return try {
            val response = api.registrarOrganizacion(dto)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(ApiException("Error al registrar organización: Código ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(ApiException("Error de red al registrar: ${e.message}"))
        }
    }
}