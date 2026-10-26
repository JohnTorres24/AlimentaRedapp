package com.example.alimentaredapp.ui.screens
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alimentaredapp.ui.screens.DetalleLoteScreen
import com.example.alimentaredapp.ui.screens.ListadoLotesScreen
import com.example.alimentaredapp.ui.screens.LoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(
                onLoginSuccess = { tokenExtraido ->
                    // Navegamos al listado pasando el token real obtenido en el login
                    navController.navigate("listado_lotes/$tokenExtraido") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("listado_lotes/{token}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""

            PublicarLoteScreen(
                token = token,
                onPublicacionExitosa = {
                    navController.popBackStack() // Regresa al listado después de publicar
                }
            )
        }

        composable("detalle_lote/{loteId}/{token}") { backStackEntry ->
            val loteId = backStackEntry.arguments?.getString("loteId")?.toLongOrNull() ?: 0L
            val token = backStackEntry.arguments?.getString("token") ?: ""

            DetalleLoteScreen(
                loteId = loteId,
                token = token,
                onReservaExitosa = {
                    navController.popBackStack() // Regresa al listado
                }
            )
        }
    }
}