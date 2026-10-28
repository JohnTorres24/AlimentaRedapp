package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.RegistroOrganizacionRequestDto
import com.example.alimentaredapp.dto.RegistroOrganizacionResponseDto
import com.example.alimentaredapp.dto.RegistroUsuarioRequestDto
import com.example.alimentaredapp.Exception.ApiException

class OrganizacionRepository {
    private val authApi = RetrofitClient.instance.create(AuthApi::class.java)
    private val orgApi = RetrofitClient.instance.create(OrganizacionApi::class.java)

    suspend fun registrarOrganizacion(dto: RegistroOrganizacionRequestDto): Result<RegistroOrganizacionResponseDto> {
        return try {
            val rolFinal = if (dto.esDonante) "DONANTE" else "RECEPTOR"
            val userDto = RegistroUsuarioRequestDto(
                nombre = dto.nombre,
                email = dto.email ?: "contacto@organizacion.org",
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
                val rawError = userResponse.errorBody()?.string() ?: ""
                val errorMessage = when {
                    userResponse.code() == 502 || rawError.contains("502") || rawError.startsWith("<!DOCTYPE") ->
                        "El servidor en Render se está iniciando. Por favor reintenta en un momento."
                    rawError.contains("Ya existe un usuario", ignoreCase = true) ->
                        "Ya existe un usuario registrado con este correo"
                    rawError.contains("email", ignoreCase = true) ->
                        "El formato del correo institucional no es válido"
                    rawError.isNotBlank() && !rawError.contains("<") ->
                        rawError
                    else ->
                        "Error en el servidor: Código ${userResponse.code()}"
                }
                Result.failure(ApiException(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(ApiException("Error de conexión al registrar: ${e.message}"))
        }
    }
}
