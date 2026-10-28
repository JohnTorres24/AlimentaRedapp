package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.LoginRequestDto
import com.example.alimentaredapp.dto.LoginResponseDto
import com.example.alimentaredapp.Exception.ApiException
import com.example.alimentaredapp.util.ErrorHelper

class AuthRepository {
    private val api = RetrofitClient.instance.create(AuthApi::class.java)

    suspend fun login(dto: LoginRequestDto): Result<LoginResponseDto> {
        return try {
            val response = api.autenticarUsuario(dto)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val rawError = response.errorBody()?.string()
                val mensaje = ErrorHelper.mapHttpError(response.code(), rawError, "iniciar sesión")
                Result.failure(ApiException(mensaje))
            }
        } catch (e: Exception) {
            val mensaje = ErrorHelper.mapNetworkException(e, "iniciar sesión")
            Result.failure(ApiException(mensaje))
        }
    }
}
