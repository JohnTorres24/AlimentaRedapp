package com.example.alimentaredapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alimentaredapp.ui.theme.StatusAvailable
import com.example.alimentaredapp.ui.theme.StatusCompleted
import com.example.alimentaredapp.ui.theme.StatusExpired
import com.example.alimentaredapp.ui.theme.StatusReserved

data class SolicitudHistorialItem(
    val id: Long,
    val loteTitulo: String,
    val cantidadSolicitada: String,
    val estado: String, // PENDIENTE, APROBADA, RECHAZADA, COMPLETADA
    val fechaSolicitud: String,
    val donante: String = "Sede Alimentos Central"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisSolicitudesScreen(
    token: String = "",
    onVolver: (() -> Unit)? = null,
    onSeguimientoClick: ((Long) -> Unit)? = null,
    onReportarIncidenciaClick: ((Long) -> Unit)? = null
) {
    var filtroEstado by remember { mutableStateOf("TODAS") }

    val todasLasSolicitudes = remember {
        listOf(
            SolicitudHistorialItem(101, "Panadería Fresca del Día", "15.0 kg", "APROBADA", "2026-10-27", "Panadería La Española"),
            SolicitudHistorialItem(102, "Lote de Verduras Variadas", "30.0 kg", "PENDIENTE", "2026-10-27", "Supermercado Ahal"),
            SolicitudHistorialItem(103, "Lácteos y Quesos Pasteurizados", "10.0 L", "COMPLETADA", "2026-10-25", "Cafetería Central"),
            SolicitudHistorialItem(104, "Granos y Legumbres Secas", "20.0 kg", "COMPLETADA", "2026-10-24", "Distribuidora Norte")
        )
    }

    val solicitudesFiltradas = remember(filtroEstado, todasLasSolicitudes) {
        if (filtroEstado == "TODAS") {
            todasLasSolicitudes
        } else {
            todasLasSolicitudes.filter { it.estado.equals(filtroEstado, ignoreCase = true) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Solicitudes y Donaciones") },
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
                text = "Historial de transacciones activas e históricas",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            // Chips de Filtro por Estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("TODAS", "APROBADA", "PENDIENTE", "COMPLETADA").forEach { estado ->
                    FilterChip(
                        selected = filtroEstado == estado,
                        onClick = { filtroEstado = estado },
                        label = {
                            Text(
                                when (estado) {
                                    "TODAS" -> "Todas"
                                    "APROBADA" -> "Aprobadas"
                                    "PENDIENTE" -> "Pendientes"
                                    "COMPLETADA" -> "Completadas"
                                    else -> estado
                                },
                                fontSize = 12.sp
                            )
                        }
                    )
                }
            }

            // Lista de Tarjetas de Solicitudes
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(solicitudesFiltradas) { solicitud ->
                    val colorEstado = when (solicitud.estado) {
                        "APROBADA" -> StatusAvailable
                        "COMPLETADA" -> StatusCompleted
                        "PENDIENTE" -> StatusReserved
                        else -> StatusExpired
                    }

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
                                    text = "Solicitud #${solicitud.id}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )

                                Surface(
                                    color = colorEstado.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(20.dp)
                                ) {
                                    Text(
                                        text = solicitud.estado,
                                        color = colorEstado,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Text(
                                text = solicitud.loteTitulo,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Scale,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colorScheme.outline
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = solicitud.cantidadSolicitada,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.CalendarToday,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = MaterialTheme.colorScheme.outline
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = solicitud.fechaSolicitud,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }

                            Divider(modifier = Modifier.padding(vertical = 4.dp))

                            // Botones de Acción
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (solicitud.estado == "APROBADA" || solicitud.estado == "PENDIENTE") {
                                    Button(
                                        onClick = {
                                            if (onSeguimientoClick != null) {
                                                onSeguimientoClick(solicitud.id)
                                            }
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary
                                        )
                                    ) {
                                        Icon(
                                            Icons.Default.LocalShipping,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Seguimiento", fontSize = 12.sp)
                                    }
                                }

                                OutlinedButton(
                                    onClick = {
                                        if (onReportarIncidenciaClick != null) {
                                            onReportarIncidenciaClick(solicitud.id)
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Icon(
                                        Icons.Default.Warning,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Incidencia", fontSize = 12.sp)
                                }
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
                    Text("Volver al Inicio")
                }
            }
        }
    }
}
