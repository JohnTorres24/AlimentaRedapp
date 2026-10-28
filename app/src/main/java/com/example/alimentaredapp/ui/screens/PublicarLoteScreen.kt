package com.example.alimentaredapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alimentaredapp.Repository.LoteRepository
import com.example.alimentaredapp.dto.PublicarLoteRequestDto
import com.example.alimentaredapp.util.SessionManager
import com.example.alimentaredapp.ViewModel.LoteUiState
import com.example.alimentaredapp.ViewModel.LoteViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicarLoteScreen(
    token: String = "",
    viewModel: LoteViewModel = viewModel(),
    onPublicacionExitosa: () -> Unit
) {
    val isAdmin = SessionManager.isAdmin
    val loteRepository = remember { LoteRepository() }

    // Fecha actual y fecha recomendada (+24 horas para cumplir con el margen de seguridad de recogida)
    val cal = Calendar.getInstance()
    val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val formatoHora = SimpleDateFormat("HH:mm", Locale.getDefault())

    val fechaHoy = formatoFecha.format(cal.time)
    cal.add(Calendar.DAY_OF_YEAR, 1)
    val fechaManana = formatoFecha.format(cal.time)

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var unidadMedida by remember { mutableStateOf("kg") }
    var fechaExpiracion by remember { mutableStateOf(fechaManana) }
    var horaExpiracion by remember { mutableStateOf("18:00") }

    // Categorías reales en Render (13: Panadería, 14: Frutas y Verduras, 15: Lácteos, 16: Granos y Cereales, 17: Comidas Preparadas)
    val categoriasDisponibles = remember {
        mutableStateListOf(
            13L to "Panadería",
            14L to "Frutas y Verduras",
            15L to "Lácteos",
            16L to "Granos y Cereales",
            17L to "Comidas Preparadas"
        )
    }

    var categoriaId by remember { mutableLongStateOf(13L) }
    var categoriaNombre by remember { mutableStateOf("Panadería") }
    var expandedCat by remember { mutableStateOf(false) }

    // Sincronizar dinámicamente las categorías reales desde el servidor en Render
    LaunchedEffect(Unit) {
        val result = loteRepository.obtenerCategorias(token)
        result.onSuccess { cats ->
            if (cats.isNotEmpty()) {
                categoriasDisponibles.clear()
                categoriasDisponibles.addAll(cats.map { it.id to it.nombre })
                val primera = cats.first()
                categoriaId = primera.id
                categoriaNombre = primera.nombre
            }
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    val cantidadParsed = cantidad.replace(',', '.').toDoubleOrNull()
    val cantidadValida = cantidadParsed != null && cantidadParsed > 0.0

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
                text = "Registra los alimentos disponibles para que organizaciones receptoras puedan solicitarlos",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            // Título o Nombre del Lote
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título del lote de alimentos *") },
                placeholder = { Text("Ej: Pan tajado fresco del día, Manzanas rojas...") },
                leadingIcon = { Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Selector Desplegable de Categoría (Sincronizado con Render)
            ExposedDropdownMenuBox(
                expanded = expandedCat,
                onExpandedChange = { expandedCat = !expandedCat },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = categoriaNombre,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría de Alimentos *") },
                    leadingIcon = { Icon(Icons.Default.Category, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCat) },
                    shape = RoundedCornerShape(12.dp),
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
                            text = { Text(nombre, fontWeight = if (id == categoriaId) FontWeight.Bold else FontWeight.Normal) },
                            leadingIcon = {
                                if (id == categoriaId) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                }
                            },
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
                    onValueChange = { input ->
                        if (input.all { it.isDigit() || it == '.' || it == ',' }) {
                            cantidad = input
                        }
                    },
                    label = { Text("Cantidad total *") },
                    placeholder = { Text("Ej: 25.0") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1.5f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = unidadMedida,
                    onValueChange = { unidadMedida = it },
                    label = { Text("Unidad *") },
                    placeholder = { Text("kg, uds, L") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            // Chips de cantidades frecuentes para facilitar el ingreso
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("5.0", "10.0", "20.0", "50.0").forEach { cantSugerida ->
                    SuggestionChip(
                        onClick = { cantidad = cantSugerida },
                        label = { Text("$cantSugerida $unidadMedida", fontSize = 11.sp) }
                    )
                }
            }

            // Fecha y Hora de Expiración
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = fechaExpiracion,
                    onValueChange = { if (isAdmin) fechaExpiracion = it },
                    readOnly = !isAdmin,
                    label = { Text("Fecha de retiro") },
                    leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
                    supportingText = {
                        Text(if (isAdmin) "Modo administrador (editable)" else "Programada (+24h)")
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1.4f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = horaExpiracion,
                    onValueChange = { if (isAdmin) horaExpiracion = it },
                    readOnly = !isAdmin,
                    label = { Text("Hora límite") },
                    leadingIcon = { Icon(Icons.Default.AccessTime, contentDescription = null) },
                    supportingText = {
                        Text(if (isAdmin) "Modo administrador (editable)" else "Hora estimada")
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
                label = { Text("Descripción y condiciones de entrega (opcional)") },
                placeholder = { Text("Ej: Empacado en cajas plásticas, conservar refrigerado...") },
                shape = RoundedCornerShape(12.dp),
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            // Estado de Publicación y Feedback Visual
            when (val state = uiState) {
                is LoteUiState.Loading -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Conectando y publicando lote de donación...", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
                is LoteUiState.SuccessPublicar -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = state.mensaje,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    LaunchedEffect(Unit) {
                        kotlinx.coroutines.delay(1200)
                        onPublicacionExitosa()
                    }
                }
                is LoteUiState.Error -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = state.message,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Botón de Publicación
            Button(
                onClick = {
                    val timestampFormatted = "${fechaExpiracion.trim()}T${horaExpiracion.trim()}:00"

                    // Organización donante segura en Render
                    val orgIdSegura = when {
                        SessionManager.organizacionId > 0 -> SessionManager.organizacionId
                        SessionManager.userId == 5L -> 2L
                        SessionManager.userId == 9L -> 5L
                        else -> 2L // "Panaderia Artesanal Chia" (Organización donante activa en Render)
                    }

                    val dto = PublicarLoteRequestDto(
                        titulo = titulo.trim(),
                        descripcion = descripcion.trim().ifBlank { "Lote de alimentos disponible para donación" },
                        cantidadTotal = cantidadParsed ?: 1.0,
                        unidadMedida = unidadMedida.trim().ifBlank { "kg" },
                        fechaExpiracion = timestampFormatted,
                        organizacionId = orgIdSegura,
                        categoriaId = categoriaId
                    )
                    viewModel.publicarLote(token, dto)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = uiState !is LoteUiState.Loading && titulo.isNotBlank() && cantidadValida
            ) {
                Icon(Icons.Default.CloudUpload, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (cantidadValida && cantidadParsed != null) {
                        "Publicar Donación (${cantidadParsed} ${unidadMedida.trim().ifBlank { "kg" }})"
                    } else {
                        "Completar datos para publicar"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            OutlinedButton(
                onClick = onPublicacionExitosa,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar y Volver")
            }
        }
    }
}
