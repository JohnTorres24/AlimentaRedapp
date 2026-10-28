package com.example.alimentaredapp.util

object ErrorHelper {

    fun mapHttpError(code: Int, rawBody: String? = null, accion: String = "realizar la operación"): String {
        val cleanBody = rawBody?.trim() ?: ""

        // Detección contextual de respuestas de negocio
        if (cleanBody.isNotBlank() && !cleanBody.contains("<html", ignoreCase = true) && cleanBody.length < 200) {
            when {
                cleanBody.contains("Categoría no encontrada", ignoreCase = true) ->
                    return "La categoría de alimentos no fue encontrada en el catálogo. Por favor selecciona una de las categorías vigentes de la lista."
                cleanBody.contains("Organización no encontrada", ignoreCase = true) ->
                    return "No se encontró la organización emisora. Por favor verifica que tu cuenta de donante esté registrada y activa."
                cleanBody.contains("No hay suficiente cantidad", ignoreCase = true) || cleanBody.contains("insuficiente", ignoreCase = true) ->
                    return "La cantidad solicitada supera el inventario disponible en este lote. Por favor reduce la cantidad a reservar."
                cleanBody.contains("Lote de alimento no encontrado", ignoreCase = true) || cleanBody.contains("Lote no encontrado", ignoreCase = true) ->
                    return "El lote de alimentos no existe o ya fue reservado en su totalidad por otra organización."
                cleanBody.contains("Usuario no encontrado", ignoreCase = true) ->
                    return "Tu sesión de usuario no pudo ser validada en el servidor. Por favor cierra sesión e ingresa de nuevo."
                cleanBody.contains("Ya existe un usuario", ignoreCase = true) ->
                    return "Ya existe una cuenta registrada con este correo electrónico en la red."
                cleanBody.contains("nit", ignoreCase = true) ->
                    return "El NIT ingresado ya se encuentra registrado con otra organización."
                cleanBody.contains("Correo o contraseña incorrectos", ignoreCase = true) ->
                    return "Correo electrónico o contraseña incorrectos. Verifica tus credenciales."
            }
        }

        return when (code) {
            400 -> "Los datos ingresados no son válidos o están incompletos. Revisa los campos obligatorios."
            401 -> "Sesión no válida o credenciales incorrectas. Por favor inicia sesión nuevamente."
            403 -> "Acceso denegado: Tu perfil no tiene permisos autorizados para $accion."
            404 -> "El elemento solicitado no fue encontrado en el servidor."
            409 -> "Ya existe un registro con información duplicada en el sistema."
            500, 502, 503, 504 -> "El servidor tardó en responder. Por favor intenta de nuevo en unos segundos."
            else -> "Ocurrió una eventualidad al $accion. Por favor intenta nuevamente."
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
            "No se pudo conectar con el servidor. Verifica tu acceso a internet o reintenta en un momento."
        } else {
            "Error de comunicación al $accion. Por favor intenta nuevamente."
        }
    }
}
