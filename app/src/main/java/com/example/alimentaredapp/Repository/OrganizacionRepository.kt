package com.example.alimentaredapp.Repository

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
            val userDto = RegistroUsuarioRequestDto(
                nombre = dto.nombre,
                email = dto.email ?: "contacto@organizacion.com",
                password = dto.passwordHash ?: "123456",
                rol = rolFinal
            )
            val userResponse = authApi.registrarUsuario(userDto)

            if (userResponse.isSuccessful && userResponse.body() != null) {
                val user = userResponse.body()!!
                try {
                    val orgReq = dto.copy(usuarioId = user.id)
                    orgApi.registrarOrganizacion(orgReq)
                } catch (_: Exception) {}
                Result.success(
                    RegistroOrganizacionResponseDto(
                        id = user.id,
                        nombre = dto.nombre,
                        nitIdentificacion = dto.nitIdentificacion,
                        direccion = dto.direccion ?: "Cajicá, Cundinamarca",
                        tipoOrganizacion = rolFinal,
                        esDonante = dto.esDonante,
                        esReceptora = dto.esReceptora,
                        usuarioId = user.id
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
