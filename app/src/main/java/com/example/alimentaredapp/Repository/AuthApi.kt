package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.LoginRequestDto
import com.example.alimentaredapp.dto.LoginResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login") // Ajusta la ruta si tu backend usa una diferente
    suspend fun autenticarUsuario(
        @Body request: LoginRequestDto
    ): Response<LoginResponseDto>
}