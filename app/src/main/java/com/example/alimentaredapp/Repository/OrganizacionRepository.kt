package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.LoginRequestDto
import com.example.alimentaredapp.dto.RegistroOrganizacionRequestDto
import com.example.alimentaredapp.dto.RegistroOrganizacionResponseDto
import com.example.alimentaredapp.dto.RegistroUsuarioRequestDto
import com.example.alimentaredapp.Exception.ApiException
import com.example.alimentaredapp.util.ErrorHelper

class OrganizacionRepository {
    private val authApi = RetrofitClient.instance.create(AuthApi::class.java)
    private val orgApi = RetrofitClient.instance.create(OrganizacionApi::class.java)

    suspend fun registrarOrganizacion(dto: RegistroOrganizacionRequestDto): Result<RegistroOrganizacionResponseDto> {
        return try {
            val rolFinal = if (dto.esDonante) "DONANTE" else "RECEPTOR"
            val emailFinal = dto.email?.trim() ?: "contacto@organizacion.com"
            val passwordFinal = dto.passwordHash ?: "123456"

            val userDto = RegistroUsuarioRequestDto(
                nombre = dto.nombre,
                email = emailFinal,
                password = passwordFinal,
                rol = rolFinal
            )
            val userResponse = authApi.registrarUsuario(userDto)

            if (userResponse.isSuccessful && userResponse.body() != null) {
                val user = userResponse.body()!!
                var orgId = user.id
                try {
                    val orgReq = dto.copy(usuarioId = user.id)
                    val orgResponse = orgApi.registrarOrganizacion(orgReq)
                    if (orgResponse.isSuccessful && orgResponse.body() != null) {
                        orgId = orgResponse.body()!!.id
                    }
                } catch (_: Exception) {}

                // Iniciar sesión inmediatamente para generar el token JWT oficial en el backend
                var tokenJwt: String? = null
                try {
                    val loginRes = authApi.autenticarUsuario(LoginRequestDto(emailFinal, passwordFinal))
                    if (loginRes.isSuccessful && loginRes.body() != null) {
                        tokenJwt = loginRes.body()!!.token
                    }
                } catch (_: Exception) {}

                Result.success(
                    RegistroOrganizacionResponseDto(
                        id = orgId,
                        nombre = dto.nombre,
                        nitIdentificacion = dto.nitIdentificacion,
                        direccion = dto.direccion ?: "Cajicá, Cundinamarca",
                        tipoOrganizacion = rolFinal,
                        esDonante = dto.esDonante,
                        esReceptora = dto.esReceptora,
                        usuarioId = user.id,
                        token = tokenJwt
                    )
                )
            } else {
                val rawError = userResponse.errorBody()?.string()
                val mensaje = ErrorHelper.mapHttpError(userResponse.code(), rawError, "registrar la organización")
                Result.failure(ApiException(mensaje))
            }
        } catch (e: Exception) {
            val mensaje = ErrorHelper.mapNetworkException(e, "registrar la organización")
            Result.failure(ApiException(mensaje))
        }
    }
}
