package com.example.alimentaredapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alimentaredapp.ui.screens.LoginScreen
import com.example.alimentaredapp.ui.screens.DetalleLoteScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Definimos "login" como la pantalla inicial
    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    // Temporalmente navegamos al detalle del lote 1 con un token simulado.
                    // Lo ideal será navegar a una pantalla de "Listado de Lotes" después.
                    navController.navigate("detalle_lote/1/token_aqui") {
                        // popUpTo evita que el usuario regrese al Login presionando el botón "Atrás"
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // Ruta que recibe parámetros (el ID del lote y el token del usuario)
        composable("detalle_lote/{loteId}/{token}") { backStackEntry ->
            val loteId = backStackEntry.arguments?.getString("loteId")?.toLongOrNull() ?: 1L
            val token = backStackEntry.arguments?.getString("token") ?: ""

            DetalleLoteScreen(
                loteId = loteId,
                token = token,
                onReservaExitosa = {
                    // Si la reserva es exitosa, regresamos a la pantalla anterior
                    navController.popBackStack()
                }
            )
        }
    }
}