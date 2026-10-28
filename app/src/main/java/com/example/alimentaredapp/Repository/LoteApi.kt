package com.example.alimentaredapp.Repository

import com.example.alimentaredapp.dto.LoteResponseDto
import com.example.alimentaredapp.dto.PublicarLoteRequestDto
import com.example.alimentaredapp.dto.PublicarLoteResponseDto
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class CategoriaResponseDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String? = null
)

interface LoteApi {
    // Listado general de lotes (soporta filtro por estado como DISPONIBLE)
    @GET("lotes")
    suspend fun obtenerLotes(
        @Header("Authorization") token: String? = null,
        @Query("estado") estado: String? = null
    ): Response<List<LoteResponseDto>>

    // Método de conveniencia para compatibilidad con código existente
    @GET("lotes")
    suspend fun obtenerLotesDisponibles(
        @Header("Authorization") token: String? = null,
        @Query("estado") estado: String? = "DISPONIBLE"
    ): Response<List<LoteResponseDto>>

    // Detalle de un lote específico por ID
    @GET("lotes/{id}")
    suspend fun obtenerLotePorId(
        @Header("Authorization") token: String? = null,
        @Path("id") id: Long
    ): Response<LoteResponseDto>

    // Categorías de alimentos configuradas en el servidor
    @GET("categorias")
    suspend fun obtenerCategorias(
        @Header("Authorization") token: String? = null
    ): Response<List<CategoriaResponseDto>>

    // Publicación de un nuevo lote
    @POST("lotes")
    suspend fun publicarLote(
        @Header("Authorization") token: String? = null,
        @Body request: PublicarLoteRequestDto
    ): Response<PublicarLoteResponseDto>
}
