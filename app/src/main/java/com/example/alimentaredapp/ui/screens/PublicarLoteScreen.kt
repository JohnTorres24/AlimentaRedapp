package com.example.alimentaredapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alimentaredapp.dto.PublicarLoteRequestDto
import com.example.alimentaredapp.util.SessionManager
import com.example.alimentaredapp.ViewModel.LoteUiState
import com.example.alimentaredapp.ViewModel.LoteViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicarLoteScreen(
    token: String,
    viewModel: LoteViewModel = viewModel(),
    onPublicacionExitosa: () -> Unit
) {
    val isAdmin = SessionManager.isAdmin

    // Requisito: Fecha y hora actuales de manera automática
    val sdfFecha = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val sdfHora = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val fechaActual = remember { sdfFecha.format(Date()) }
    val horaActual = remember { sdfHora.format(Date()) }

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var unidadMedida by remember { mutableStateOf("kg") }
    var fechaExpiracion by remember { mutableStateOf(fechaActual) }
    var horaExpiracion by remember { mutableStateOf(horaActual) }
    var categoriaId by remember { mutableStateOf(1L) }
    var categoriaNombre by remember { mutableStateOf("Panadería") }
    var expandedCat by remember { mutableStateOf(false) }

    val categoriasDisponibles = listOf(
        1L to "Panadería y Masas",
        2L to "Frutas y Verduras",
        3L to "Lácteos y Derivados",
        4L to "Comidas Preparadas",
        5L to "Granos y No Perecederos"
    )

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Publicar Lote de Alimentos") },
                navigationIcon = {
                    IconButton(onClick = onPublicacionExitosa) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Ingrese los datos del lote disponible para donación",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            // Título o Nombre del Lote
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título del lote (ej. Panadería del día)") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Selector Desplegable de Categoría
            ExposedDropdownMenuBox(
                expanded = expandedCat,
                onExpandedChange = { expandedCat = !expandedCat },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = categoriaNombre,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría del Alimento") },
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCat) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedCat,
                    onDismissRequest = { expandedCat = false }
                ) {
                    categoriasDisponibles.forEach { (id, nombre) ->
                        DropdownMenuItem(
                            text = { Text(nombre) },
                            onClick = {
                                categoriaId = id
                                categoriaNombre = nombre
                                expandedCat = false
                            }
                        )
                    }
                }
            }

            // Cantidad y Unidad en una fila
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { cantidad = it },
                    label = { Text("Cantidad total") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1.5f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = unidadMedida,
                    onValueChange = { unidadMedida = it },
                    label = { Text("Unidad (kg, L, uds)") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            // Fecha y Hora (Requisito: Fecha y hora automáticas actuales, no editables excepto por admin)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = fechaExpiracion,
                    onValueChange = { if (isAdmin) fechaExpiracion = it },
                    readOnly = !isAdmin,
                    label = { Text("Fecha actual") },
                    supportingText = {
                        Text(if (isAdmin) "Modo administrador (editable)" else "Automática (no editable)")
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1.4f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = horaExpiracion,
                    onValueChange = { if (isAdmin) horaExpiracion = it },
                    readOnly = !isAdmin,
                    label = { Text("Hora actual") },
                    supportingText = {
                        Text(if (isAdmin) "Modo administrador (editable)" else "Automática (no editable)")
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1.1f),
                    singleLine = true
                )
            }

            // Descripción adicional
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción o condiciones de empaque") },
                shape = RoundedCornerShape(12.dp),
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            // Estado de Publicación
            when (val state = uiState) {
                is LoteUiState.Loading -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            // Requisito: Solo indicar que se está conectando
                            Text("Conectando y publicando lote...", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
                is LoteUiState.SuccessPublicar -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Text(
                            text = state.mensaje,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                    LaunchedEffect(Unit) {
                        onPublicacionExitosa()
                    }
                }
                is LoteUiState.Error -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Botón de Publicación
            Button(
                onClick = {
                    val timestampFormatted = "${fechaExpiracion.trim()}T${horaExpiracion.trim()}:00"
                    val dto = PublicarLoteRequestDto(
                        titulo = titulo.trim(),
                        descripcion = descripcion.trim(),
                        cantidadTotal = cantidad.toDoubleOrNull() ?: 1.0,
                        unidadMedida = unidadMedida.trim().ifBlank { "kg" },
                        fechaExpiracion = timestampFormatted,
                        organizacionId = 1L,
                        categoriaId = categoriaId
                    )
                    viewModel.publicarLote(token, dto)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = uiState !is LoteUiState.Loading && titulo.isNotBlank() && cantidad.isNotBlank()
            ) {
                Text("Publicar Lote en la Red", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = onPublicacionExitosa,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}
