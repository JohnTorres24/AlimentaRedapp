package com.example.alimentaredapp.util

object SessionManager {
    var token: String = ""
    var userRole: String = "DONANTE"
    var userId: Long = 0L
    var userEmail: String = ""

    /**
     * Valida de manera estricta si el usuario actual tiene autorización para
     * acceder al panel de Administrador.
     *
     * Reglas requeridas:
     * 1. Análisis de Actor: Cuentas con actor DONANTE o RECEPTOR bajo ninguna circunstancia
     *    pueden acceder al panel de administrador.
     * 2. Análisis de Correo Electrónico: El correo debe corresponder a una cuenta
     *    administrativa oficial (ej: admin@..., *@alimentared.com, administrador@...).
     * 3. Únicamente usuarios verificados como Administrador tienen acceso concedido.
     */
    val isAdmin: Boolean
        get() {
            val actor = userRole.trim().uppercase()
            val email = userEmail.trim().lowercase()

            // Si el actor es explícitamente DONANTE o RECEPTOR, denegación absoluta
            if (actor == "DONANTE" || actor == "RECEPTOR" || actor.contains("ORGANIZACION") || actor.contains("DONACION")) {
                return false
            }

            // Validar si el rol es de administrador
            val tieneRolAdmin = actor == "ADMIN" || actor == "ADMINISTRADOR"

            // Analizar la estructura del correo para privilegios administrativos
            val tieneEmailAdmin = email.startsWith("admin") ||
                    email.contains("admin@") ||
                    email.contains("administrador") ||
                    email.endsWith("@alimentared.com") ||
                    email.contains(".admin@")

            // Debe tener rol de admin o correo verificado administrativo (sin ser donante ni receptor)
            return tieneRolAdmin || tieneEmailAdmin
        }

    fun cerrarSesion() {
        token = ""
        userRole = "DONANTE"
        userId = 0L
        userEmail = ""
    }
}
