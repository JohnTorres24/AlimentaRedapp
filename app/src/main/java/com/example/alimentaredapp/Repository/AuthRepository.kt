package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.LoginRequestDto
import com.example.alimentaredapp.dto.LoginResponseDto
import com.example.alimentaredapp.Exception.ApiException

class AuthRepository {
    private val api = RetrofitClient.instance.create(AuthApi::class.java)

    suspend fun login(dto: LoginRequestDto): Result<LoginResponseDto> {
        return try {
            val response = api.autenticarUsuario(dto)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                // Manejo de credenciales incorrectas (ej. error 401 o 403)
                Result.failure(ApiException("Error de autenticación: Credenciales inválidas o código ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(ApiException("Error de conexión con el servidor: ${e.message}"))
        }
    }
}