package com.example.alimentared.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

// Modelo de prueba temporal para las solicitudes
data class SolicitudItem(
    val id: Long,
    val loteTitulo: String,
    val cantidadSolicitada: String,
    val estado: String, // PENDIENTE, APROBADA, RECHAZADA, COMPLETADA
    val fechaSolicitud: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisSolicitudesScreen(
    onVolver: () -> Unit
) {
    // Lista de ejemplo simulando datos del historial de solicitudes/donaciones
    val solicitudesEjemplo = listOf(
        SolicitudItem(1, "Panadería del Día", "5.0 kg", "APROBADA", "2026-10-25"),
        SolicitudItem(2, "Verduras Frescas", "10.0 kg", "PENDIENTE", "2026-10-25"),
        SolicitudItem(3, "Lotes de Lácteos", "2.0 Litros", "COMPLETADA", "2026-10-24")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Solicitudes y Donaciones") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Historial de transacciones activas e históricas (RF09)[cite: 11]",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            // Lista de solicitudes
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(solicitudesEjemplo) { solicitud ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = solicitud.loteTitulo,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = solicitud.estado,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = when (solicitud.estado) {
                                        "APROBADA" -> MaterialTheme.colorScheme.tertiary
                                        "COMPLETADA" -> MaterialTheme.colorScheme.primary
                                        "PENDIENTE" -> MaterialTheme.colorScheme.secondary
                                        else -> MaterialTheme.colorScheme.error
                                    }
                                )
                            }
                            Text(text = "Cantidad solicitada: ${solicitud.cantidadSolicitada}")
                            Text(text = "Fecha: ${solicitud.fechaSolicitud}", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                        }
                    }
                }
            }

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}


