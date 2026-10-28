package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.LoginRequestDto
import com.example.alimentaredapp.dto.LoginResponseDto
import com.example.alimentaredapp.dto.RegistroUsuarioRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun autenticarUsuario(
        @Body request: LoginRequestDto
    ): Response<LoginResponseDto>

    @POST("usuarios/register")
    suspend fun registrarUsuario(
        @Body request: RegistroUsuarioRequestDto
    ): Response<LoginResponseDto>
}
