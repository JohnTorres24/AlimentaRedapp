package com.example.alimentaredapp.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alimentaredapp.Repository.LoteRepository
import com.example.alimentaredapp.dto.LoteResponseDto
import com.example.alimentaredapp.ui.theme.StatusAvailable
import com.example.alimentaredapp.ui.theme.StatusExpired
import com.example.alimentaredapp.ViewModel.SolicitudUiState
import com.example.alimentaredapp.ViewModel.SolicitudViewModel
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleLoteScreen(
    loteId: Long,
    token: String,
    viewModel: SolicitudViewModel = viewModel(),
    onReservaExitosa: () -> Unit
) {
    var cantidad by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }
    var loteDetalle by remember { mutableStateOf<LoteResponseDto?>(null) }
    var cargandoDetalle by remember { mutableStateOf(true) }

    val loteRepository = remember { LoteRepository() }
    val uiState by viewModel.uiState.collectAsState()

    // Cargar información real del lote desde el backend en Render
    LaunchedEffect(loteId, token) {
        cargandoDetalle = true
        val result = loteRepository.obtenerLotePorId(token, loteId)
        result.fold(
            onSuccess = { dto ->
                loteDetalle = dto
                // Inicializar con el 50% de la cantidad disponible por defecto para facilitar al usuario
                val inicial = (dto.cantidadDisponible * 0.5)
                val redondeado = String.format(Locale.US, "%.1f", if (inicial > 0) inicial else dto.cantidadDisponible)
                cantidad = redondeado
                cargandoDetalle = false
            },
            onFailure = {
                cargandoDetalle = false
            }
        )
    }

    val disponibleMax = loteDetalle?.cantidadDisponible ?: 20.0
    val unidad = loteDetalle?.unidadMedida ?: "kg"
    val tituloLote = loteDetalle?.titulo ?: "Lote de Alimentos #$loteId"
    val organizacion = loteDetalle?.nombreOrganizacion ?: "Organización Donante Registrada"
    val categoria = loteDetalle?.nombreCategoria ?: "Alimentos Varios"

    // Parseo numérico seguro admitiendo punto y coma decimal
    val cantidadParsed = cantidad.replace(',', '.').toDoubleOrNull()
    val cantidadValida = cantidadParsed != null && cantidadParsed > 0.0 && cantidadParsed <= disponibleMax

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle y Solicitud de Lote") },
                navigationIcon = {
                    IconButton(onClick = onReservaExitosa) {
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tarjeta Principal del Lote
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Lote #$loteId · $categoria",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "Disponible para Rescate",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Text(
                        text = tituloLote,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    loteDetalle?.descripcion?.takeIf { it.isNotBlank() }?.let { desc ->
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    // Resaltado de Cantidad Disponible
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Inventory2, contentDescription = null, tint = StatusAvailable, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Inventario disponible en este lote:",
                                    fontSize = 12.sp,
                                    color = Color(0xFF2E7D32)
                                )
                                Text(
                                    text = "$disponibleMax $unidad",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20)
                                )
                            }
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Store, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Donante: $organizacion", style = MaterialTheme.typography.bodyMedium)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccessTime, contentDescription = null, tint = StatusExpired, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Fecha límite de recogida: ${loteDetalle?.fechaExpiracion?.replace("T", " ") ?: "Consultar al retirar"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = StatusExpired
                        )
                    }
                }
            }

            // Sección Interactiva de Solicitud de Cantidad
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "1. Selecciona la cantidad que necesitas",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "Puedes solicitar desde una fracción hasta el total disponible ($disponibleMax $unidad):",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Chips de Selección Rápida Porcentual
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val opciones = listOf(
                            "25%" to 0.25,
                            "50%" to 0.50,
                            "75%" to 0.75,
                            "Todo" to 1.00
                        )

                        opciones.forEach { (label, porcentaje) ->
                            val valorCalculado = (disponibleMax * porcentaje)
                            val valorTexto = String.format(Locale.US, "%.1f", valorCalculado)
                            val seleccionado = cantidad == valorTexto

                            FilterChip(
                                selected = seleccionado,
                                onClick = {
                                    cantidad = valorTexto
                                },
                                label = {
                                    Text(
                                        text = "$label ($valorTexto $unidad)",
                                        fontSize = 11.sp,
                                        fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Controles de Entrada Numérica y Stepper (- y +)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Botón Disminuir
                        FilledTonalIconButton(
                            onClick = {
                                val actual = cantidadParsed ?: disponibleMax
                                val nuevo = (actual - 1.0).coerceAtLeast(0.5)
                                cantidad = String.format(Locale.US, "%.1f", nuevo)
                            },
                            enabled = (cantidadParsed ?: 0.0) > 0.5
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Menos")
                        }

                        // Campo Numérico Directo
                        OutlinedTextField(
                            value = cantidad,
                            onValueChange = { input ->
                                // Permitir dígitos y coma o punto decimal
                                if (input.all { it.isDigit() || it == '.' || it == ',' }) {
                                    cantidad = input
                                }
                            },
                            label = { Text("Cantidad a solicitar") },
                            placeholder = { Text("Ej: 5.0") },
                            suffix = {
                                Text(
                                    text = unidad,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            isError = cantidad.isNotBlank() && !cantidadValida,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )

                        // Botón Aumentar
                        FilledTonalIconButton(
                            onClick = {
                                val actual = cantidadParsed ?: 0.0
                                val nuevo = (actual + 1.0).coerceAtMost(disponibleMax)
                                cantidad = String.format(Locale.US, "%.1f", nuevo)
                            },
                            enabled = (cantidadParsed ?: 0.0) < disponibleMax
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Más")
                        }
                    }

                    // Indicador de Validación en Tiempo Real
                    when {
                        cantidad.isBlank() -> {
                            Text(
                                text = "Elige una opción arriba o escribe la cantidad exacta requerida.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        cantidadParsed == null || cantidadParsed <= 0.0 -> {
                            Text(
                                text = "⚠️ Por favor ingresa un número mayor a cero.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        cantidadParsed > disponibleMax -> {
                            Text(
                                text = "⚠️ La cantidad supera el máximo disponible en el lote ($disponibleMax $unidad).",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        else -> {
                            Text(
                                text = "✓ Válido: Vas a reservar $cantidadParsed $unidad de $disponibleMax disponibles.",
                                fontSize = 12.sp,
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "2. Notas u observaciones de recogida (Opcional)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    OutlinedTextField(
                        value = observaciones,
                        onValueChange = { observaciones = it },
                        placeholder = { Text("Ej: Pasaremos a recoger en camioneta a las 3:00 PM...") },
                        minLines = 2,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Feedback visual del estado de red
            when (val state = uiState) {
                is SolicitudUiState.Loading -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Conectando y registrando solicitud de reserva...", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
                is SolicitudUiState.Success -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                    ) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusAvailable)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = state.mensaje,
                                color = Color(0xFF1B5E20),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    LaunchedEffect(Unit) {
                        kotlinx.coroutines.delay(1200)
                        onReservaExitosa()
                    }
                }
                is SolicitudUiState.Error -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = state.error,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                else -> {}
            }

            // Botón de Envío Dinámico y Descriptivo
            Button(
                onClick = {
                    if (cantidadValida && cantidadParsed != null) {
                        viewModel.solicitarReserva(token, loteId, cantidadParsed, observaciones.trim())
                    }
                },
                enabled = uiState !is SolicitudUiState.Loading && cantidadValida,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (cantidadValida && cantidadParsed != null) {
                        "Confirmar Reserva de $cantidadParsed $unidad"
                    } else {
                        "Ingresa una cantidad válida"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            OutlinedButton(
                onClick = onReservaExitosa,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al Listado de Lotes")
            }
        }
    }
}
