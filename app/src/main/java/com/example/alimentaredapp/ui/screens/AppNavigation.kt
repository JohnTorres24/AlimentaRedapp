package com.example.alimentaredapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("registro") {
            RegistroOrganizacionScreen(
                onRegistroExitoso = {
                    navController.navigate("main_flow/session_token") {
                        popUpTo("registro") { inclusive = true }
                    }
                },
                onIrAlLogin = {
                    navController.navigate("login") {
                        popUpTo("registro") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                onLoginSuccess = { tokenExtraido ->
                    navController.navigate("main_flow/$tokenExtraido") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onIrAlRegistro = {
                    navController.navigate("registro")
                }
            )
        }

        // Flujo Principal Autenticado con Bottom Navigation Bar (Lotes, Donar, Historial, Entrega, Métricas)
        composable("main_flow/{token}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""

            MainFlowScreen(
                token = token,
                onNavigateToDetalle = { loteId ->
                    navController.navigate("detalle_lote/$loteId/$token")
                },
                onNavigateToIncidencia = { idEntrega ->
                    navController.navigate("reportar_incidencia/$idEntrega/$token")
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("main_flow/{token}") { inclusive = true }
                    }
                }
            )
        }

        // Compatibilidad hacia atrás si algún enlace interno navega a listado_lotes/{token}
        composable("listado_lotes/{token}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            MainFlowScreen(
                token = token,
                onNavigateToDetalle = { loteId ->
                    navController.navigate("detalle_lote/$loteId/$token")
                },
                onNavigateToIncidencia = { idEntrega ->
                    navController.navigate("reportar_incidencia/$idEntrega/$token")
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("listado_lotes/{token}") { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de Detalle y Reserva de Lote
        composable("detalle_lote/{loteId}/{token}") { backStackEntry ->
            val loteId = backStackEntry.arguments?.getString("loteId")?.toLongOrNull() ?: 0L
            val token = backStackEntry.arguments?.getString("token") ?: ""

            DetalleLoteScreen(
                loteId = loteId,
                token = token,
                onReservaExitosa = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla de Publicar Lote individual
        composable("publicar_lote/{token}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""

            PublicarLoteScreen(
                token = token,
                onPublicacionExitosa = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla de Confirmar Entrega
        composable("confirmar_entrega/{idEntrega}/{token}") { backStackEntry ->
            val idEntrega = backStackEntry.arguments?.getString("idEntrega")?.toLongOrNull() ?: 101L
            val token = backStackEntry.arguments?.getString("token") ?: ""

            ConfirmarEntregaScreen(
                idEntrega = idEntrega,
                token = token,
                onConfirmacionExitosa = {
                    navController.popBackStack()
                },
                onReportarIncidenciaClick = { id ->
                    navController.navigate("reportar_incidencia/$id/$token")
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla de Reporte de Incidencias
        composable("reportar_incidencia/{idEntrega}/{token}") { backStackEntry ->
            val idEntrega = backStackEntry.arguments?.getString("idEntrega")?.toLongOrNull() ?: 101L
            val token = backStackEntry.arguments?.getString("token") ?: ""

            ReportarIncidenciaScreen(
                idEntrega = idEntrega,
                token = token,
                onReporteExitoso = {
                    navController.popBackStack()
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun MainFlowScreen(
    token: String,
    onNavigateToDetalle: (Long) -> Unit,
    onNavigateToIncidencia: (Long) -> Unit,
    onLogout: () -> Unit
) {
    var currentTab by remember { mutableStateOf("lotes") }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            when (currentTab) {
                "lotes" -> ListadoLotesScreen(
                    token = token,
                    onLoteClick = onNavigateToDetalle,
                    onPublicarClick = { currentTab = "donar" }
                )
                "donar" -> PublicarLoteScreen(
                    token = token,
                    onPublicacionExitosa = { currentTab = "lotes" }
                )
                "historial" -> MisSolicitudesScreen(
                    token = token,
                    onVolver = { currentTab = "lotes" },
                    onSeguimientoClick = { currentTab = "entrega" },
                    onReportarIncidenciaClick = onNavigateToIncidencia
                )
                "entrega" -> ConfirmarEntregaScreen(
                    idEntrega = 101L,
                    token = token,
                    onConfirmacionExitosa = { currentTab = "metricas" },
                    onReportarIncidenciaClick = onNavigateToIncidencia,
                    onVolver = { currentTab = "lotes" }
                )
                "metricas" -> IndicadoresScreen(
                    token = token,
                    onVolver = { currentTab = "lotes" }
                )
            }
        }

        // Barra de Navegación Inferior (Bottom Navigation Bar de los Mockups en Fondo Blanco)
        Surface(
            tonalElevation = 4.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Storefront, contentDescription = "Lotes") },
                    label = { Text("Lotes") },
                    selected = currentTab == "lotes",
                    onClick = { currentTab = "lotes" }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.AddCircle, contentDescription = "Donar") },
                    label = { Text("+ Donar") },
                    selected = currentTab == "donar",
                    onClick = { currentTab = "donar" }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Assignment, contentDescription = "Historial") },
                    label = { Text("Historial") },
                    selected = currentTab == "historial",
                    onClick = { currentTab = "historial" }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.LocalShipping, contentDescription = "Entrega") },
                    label = { Text("Entrega") },
                    selected = currentTab == "entrega",
                    onClick = { currentTab = "entrega" }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.BarChart, contentDescription = "Métricas") },
                    label = { Text("Métricas") },
                    selected = currentTab == "metricas",
                    onClick = { currentTab = "metricas" }
                )
            }
        }
    }
}
