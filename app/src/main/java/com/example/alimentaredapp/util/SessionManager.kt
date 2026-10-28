package com.example.alimentaredapp.util

object SessionManager {
    var token: String = ""
    var userRole: String = "DONANTE"
    var userId: Long = 0L
    var userEmail: String = ""

    val isAdmin: Boolean
        get() = userRole.equals("ADMINISTRADOR", ignoreCase = true) ||
                userRole.equals("ADMIN", ignoreCase = true)
}
