package com.example.alimentaredapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alimentaredapp.ViewModel.IncidenciaUiState
import com.example.alimentaredapp.ViewModel.IncidenciaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportarIncidenciaScreen(
    idEntrega: Long = 101L,
    token: String = "",
    viewModel: IncidenciaViewModel = viewModel(),
    onReporteExitoso: () -> Unit = {},
    onVolver: (() -> Unit)? = null
) {
    val causasPredefinidas = listOf(
        "Alimentos en mal estado / expirados",
        "Inasistencia del donante en punto de entrega",
        "Inasistencia de la organización receptora",
        "Cantidad entregada menor a la acordada",
        "Empaque dañado o problemas de inocuidad",
        "Horario o dirección no coincide",
        "Otro motivo"
    )

    var expandedDropdown by remember { mutableStateOf(false) }
    var motivoSeleccionado by remember { mutableStateOf(causasPredefinidas[0]) }
    var descripcion by remember { mutableStateOf("") }
    var reporteLocalExitoso by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reportar Problema de Entrega") },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Banner de Información sobre Incidencias
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Gestión de Incidencias (RF08)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = "Si ocurrió algún contratiempo con el lote o la entrega #$idEntrega, infórmalo para revisión del equipo.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Selector Desplegable de Motivo
            Text(
                text = "Motivo del Problema",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            ExposedDropdownMenuBox(
                expanded = expandedDropdown,
                onExpandedChange = { expandedDropdown = !expandedDropdown },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = motivoSeleccionado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Selecciona la causa") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(10.dp)
                )

                ExposedDropdownMenu(
                    expanded = expandedDropdown,
                    onDismissRequest = { expandedDropdown = false }
                ) {
                    causasPredefinidas.forEach { causa ->
                        DropdownMenuItem(
                            text = { Text(causa) },
                            onClick = {
                                motivoSeleccionado = causa
                                expandedDropdown = false
                            }
                        )
                    }
                }
            }

            // Campo de Descripción Detallada
            Text(
                text = "Detalles de lo Sucedido",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Describe detalladamente lo ocurrido") },
                placeholder = { Text("Ej. Al llegar al lugar acordado los empaques estaban abiertos...") },
                leadingIcon = {
                    Icon(Icons.Default.Description, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                shape = RoundedCornerShape(10.dp)
            )

            // Manejo de Estados (Loading, Success, Error)
            when (val state = uiState) {
                is IncidenciaUiState.Loading -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Registrando incidencia en el servidor...", fontSize = 14.sp)
                    }
                }
                is IncidenciaUiState.Success -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = state.data.mensaje.ifBlank { "Incidencia registrada correctamente." },
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    LaunchedEffect(Unit) {
                        onReporteExitoso()
                    }
                }
                is IncidenciaUiState.Error -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(12.dp),
                            fontSize = 13.sp
                        )
                    }
                }
                else -> {}
            }

            if (reporteLocalExitoso) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(
                        text = "Incidencia guardada exitosamente para mediación.",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(14.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón de Enviar Reporte
            Button(
                onClick = {
                    if (token.isNotBlank()) {
                        viewModel.reportarIncidencia(token, idEntrega, motivoSeleccionado, descripcion)
                    } else {
                        reporteLocalExitoso = true
                        onReporteExitoso()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = uiState !is IncidenciaUiState.Loading && descripcion.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Warning, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Enviar Reporte de Incidencia", fontWeight = FontWeight.Bold)
            }

            // Botón Cancelar
            if (onVolver != null) {
                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Cancelar y Volver")
                }
            }
        }
    }
}
