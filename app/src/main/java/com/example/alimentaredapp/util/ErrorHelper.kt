package com.example.alimentaredapp.util

object ErrorHelper {

    fun mapHttpError(code: Int, rawBody: String? = null, accion: String = "realizar la operación"): String {
        val cleanBody = rawBody?.trim() ?: ""

        // Si el cuerpo contiene mensajes específicos de las reglas de negocio
        if (cleanBody.isNotBlank() && !cleanBody.contains("<html", ignoreCase = true) && cleanBody.length < 150) {
            when {
                cleanBody.contains("Ya existe un usuario", ignoreCase = true) ->
                    return "Ya existe una cuenta registrada con este correo electrónico."
                cleanBody.contains("nit", ignoreCase = true) ->
                    return "El NIT ingresado ya se encuentra registrado en la red."
                cleanBody.contains("Categoría no encontrada", ignoreCase = true) ->
                    return "La categoría de alimentos seleccionada no es válida."
                cleanBody.contains("Lote no encontrado", ignoreCase = true) ->
                    return "El lote de alimentos no existe o ya no está disponible."
                cleanBody.contains("insuficiente", ignoreCase = true) ->
                    return "La cantidad solicitada supera la disponibilidad del lote."
                cleanBody.contains("Correo o contraseña incorrectos", ignoreCase = true) ->
                    return "Correo electrónico o contraseña incorrectos."
            }
        }

        return when (code) {
            400 -> "Los datos ingresados no son válidos o están incompletos."
            401 -> "Correo o contraseña incorrectos. Por favor verifica tus credenciales."
            403 -> "No tienes permisos suficientes para $accion."
            404 -> "El elemento solicitado no fue encontrado."
            409 -> "Ya existe un registro con esta información en el sistema."
            500, 502, 503, 504 -> "El servidor se está iniciando. Por favor reintenta en unos momentos."
            else -> "Ocurrió un problema al $accion. Por favor intenta de nuevo."
        }
    }

    fun mapNetworkException(e: Exception, accion: String = "la conexión"): String {
        val msg = e.message ?: ""
        return if (msg.contains("timeout", ignoreCase = true) ||
            msg.contains("Unable to resolve host", ignoreCase = true) ||
            msg.contains("Failed to connect", ignoreCase = true) ||
            msg.contains("Connection refused", ignoreCase = true) ||
            msg.contains("La conexión ha terminado", ignoreCase = true)
        ) {
            "No fue posible conectar con el servidor. Revisa tu conexión a internet o intenta en un momento."
        } else {
            "Error de comunicación al $accion. Por favor intenta nuevamente."
        }
    }
}
