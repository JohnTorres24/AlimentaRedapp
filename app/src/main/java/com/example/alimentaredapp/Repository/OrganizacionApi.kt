package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.RegistroOrganizacionRequestDto
import com.example.alimentaredapp.dto.RegistroOrganizacionResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OrganizacionApi {
    @POST("organizaciones/registro")
    suspend fun registrarOrganizacion(
        @Body request: RegistroOrganizacionRequestDto
    ): Response<RegistroOrganizacionResponseDto>
}