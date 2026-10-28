package com.example.alimentaredapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
    onVolver: (() -> Unit)? = null
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
                navigationIcon = {
                    if (onVolver != null) {
                        IconButton(onClick = onVolver) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                },
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
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(despachosActivos) { despacho ->
                    val esUrgente = despacho.tiempoRestante.contains("mins")
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Op. #${despacho.idOperacion}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.outline,
                                    fontWeight = FontWeight.Bold
                                )

                                Surface(
                                    color = if (esUrgente)
                                        MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
                                    else
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(20.dp)
                                ) {
                                    Text(
                                        text = despacho.estadoLogistico,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (esUrgente) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Text(
                                text = despacho.loteTitulo,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Store, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.outline)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Origen: ${despacho.donante}", fontSize = 13.sp)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.outline)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Destino: ${despacho.receptor}", fontSize = 13.sp)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.AccessTime,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = if (esUrgente) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Límite de rescate: ${despacho.tiempoRestante}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (esUrgente) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }
            }

            if (onVolver != null) {
                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Volver")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GestionLogisticaScreenPreview() {
    GestionLogisticaScreen(onVolver = {})
}
