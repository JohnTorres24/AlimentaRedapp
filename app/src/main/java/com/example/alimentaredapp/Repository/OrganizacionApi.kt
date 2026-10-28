package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.RegistroOrganizacionRequestDto
import com.example.alimentaredapp.dto.RegistroOrganizacionResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrganizacionApi {
    @POST("organizaciones")
    suspend fun registrarOrganizacion(
        @Body request: RegistroOrganizacionRequestDto,
        @Header("Authorization") token: String? = null
    ): Response<RegistroOrganizacionResponseDto>

    @GET("organizaciones")
    suspend fun listarOrganizaciones(
        @Header("Authorization") token: String? = null
    ): Response<List<RegistroOrganizacionResponseDto>>

    @GET("organizaciones/{id}")
    suspend fun obtenerOrganizacion(
        @Path("id") id: Long,
        @Header("Authorization") token: String? = null
    ): Response<RegistroOrganizacionResponseDto>

    @PUT("organizaciones/{id}")
    suspend fun actualizarOrganizacion(
        @Path("id") id: Long,
        @Body request: RegistroOrganizacionRequestDto,
        @Header("Authorization") token: String? = null
    ): Response<RegistroOrganizacionResponseDto>

    @DELETE("organizaciones/{id}")
    suspend fun eliminarOrganizacion(
        @Path("id") id: Long,
        @Header("Authorization") token: String? = null
    ): Response<Unit>
}
