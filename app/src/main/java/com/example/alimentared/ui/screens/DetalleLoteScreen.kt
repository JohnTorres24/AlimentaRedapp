package com.example.alimentared.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleLoteScreen(
    tituloLote: String,
    categoriaLote: String,
    cantidadDisponible: String,
    fechaExpiracion: String,
    donanteNombre: String,
    onSolicitarReserva: (String, String) -> Unit,
    onVolver: () -> Unit
) {
    var cantidadSolicitada by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }
    var mensajeFeedback by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle y Solicitud de Lote") },
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
            // Tarjeta con la información general del lote
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = tituloLote,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(text = "Categoría: $categoriaLote")
                    Text(text = "Cantidad Disponible: $cantidadDisponible")
                    Text(text = "Fecha de Expiración: $fechaExpiracion", color = MaterialTheme.colorScheme.error)
                    Text(text = "Organización Donante: $donanteNombre", fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Realizar Solicitud de Reserva (RF05)",
                style = MaterialTheme.typography.titleMedium
            )

            // Campo para ingresar la cantidad que desea solicitar
            OutlinedTextField(
                value = cantidadSolicitada,
                onValueChange = { cantidadSolicitada = it },
                label = { Text("Cantidad a solicitar") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Observaciones adicionales opcionales
            OutlinedTextField(
                value = observaciones,
                onValueChange = { observaciones = it },
                label = { Text("Observaciones o notas de retiro") },
                modifier = Modifier.fillMaxWidth()
            )

            if (mensajeFeedback != null) {
                Text(
                    text = mensajeFeedback!!,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de confirmación de reserva (Conecta con POST /api/solicitudes)[cite: 7, 12]
            Button(
                onClick = {
                    if (cantidadSolicitada.isBlank()) {
                        mensajeFeedback = "Debes ingresar una cantidad válida para solicitar."
                    } else {
                        mensajeFeedback = "¡Solicitud generada con éxito!"
                        onSolicitarReserva(cantidadSolicitada, observaciones)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Solicitar Reserva de Lote")
            }

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al Listado")
            }
        }
    }
}
