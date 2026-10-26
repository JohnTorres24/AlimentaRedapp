package com.example.alimentaredapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

data class MonitoreoDespachoItem(
    val idOperacion: Long,
    val loteTitulo: String,
    val donante: String,
    val receptor: String,
    val estadoLogistico: String,
    val tiempoRestante: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionLogisticaScreen(
    onVolver: () -> Unit
) {
    val despachosActivos = listOf(
        MonitoreoDespachoItem(101, "Lote Verduras Variadas (30 kg)", "Supermercado Ahal", "Comedor La Esperanza", "En Ruta / Recolección", "2 horas"),
        MonitoreoDespachoItem(102, "Lote Panadería del Día (15 kg)", "Panadería La Española", "Asociación Pan de Vida", "Pendiente Confirmación", "4 horas"),
        MonitoreoDespachoItem(103, "Lácteos y Quesos (10 L)", "Cafetería Central", "Fundación San José", "Urgente / Próximo a Vencer", "30 mins")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Coordinación - Control Logístico") },
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
                text = "Monitoreo general de despachos y tiempos de recolección en la red",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(despachosActivos) { despacho ->
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
                                    text = "Op. #${despacho.idOperacion}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.outline
                                )
                                Text(
                                    text = despacho.estadoLogistico,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (despacho.tiempoRestante.contains("mins"))
                                        MaterialTheme.colorScheme.error
                                    else
                                        MaterialTheme.colorScheme.primary
                                )
                            }

                            Text(
                                text = despacho.loteTitulo,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(text = "Origen: ${despacho.donante}", fontSize = 12.sp)
                            Text(text = "Destino: ${despacho.receptor}", fontSize = 12.sp)
                            Text(
                                text = "Límite de rescate: ${despacho.tiempoRestante}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.error
                            )
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

@Preview(showBackground = true)
@Composable
fun GestionLogisticaScreenPreview() {
    GestionLogisticaScreen(onVolver = {})
}