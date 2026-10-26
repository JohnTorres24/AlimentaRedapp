package com.example.alimentaredapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeguimientoEntregaScreen(
    idSolicitud: Long,
    loteTitulo: String,
    puntoRecoleccion: String,
    onConfirmarEntrega: (String) -> Unit,
    onVolver: () -> Unit
) {
    var codigoVerificacion by remember { mutableStateOf("") }
    var observacionesEntrega by remember { mutableStateOf("") }
    var mensajeEstado by remember { mutableStateOf<String?>(null) }
    var entregaRealizada by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seguimiento y Confirmación") },
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
            // Tarjeta de estado del pedido
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (entregaRealizada)
                        MaterialTheme.colorScheme.secondaryContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Solicitud #$idSolicitud",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = loteTitulo,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(text = "Punto de recolección: $puntoRecoleccion")
                    Text(
                        text = if (entregaRealizada) "Estado: ENTREGADO Y COMPLETADO" else "Estado: EN PROCESO DE RETIRO",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (entregaRealizada) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
                    )
                }
            }

            if (!entregaRealizada) {
                Text(
                    text = "Confirmación de Retiro en Sitio",
                    style = MaterialTheme.typography.titleMedium
                )

                // Campo para ingresar o escanear el código de verificación
                OutlinedTextField(
                    value = codigoVerificacion,
                    onValueChange = { codigoVerificacion = it },
                    label = { Text("Código de Verificación / PIN de Entrega") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Observaciones del estado del alimento entregado
                OutlinedTextField(
                    value = observacionesEntrega,
                    onValueChange = { observacionesEntrega = it },
                    label = { Text("Notas de entrega (ej. empaque sellado, buen estado)") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (mensajeEstado != null) {
                    Text(
                        text = mensajeEstado!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón para validar la entrega
                Button(
                    onClick = {
                        if (codigoVerificacion.isBlank()) {
                            mensajeEstado = "Ingresa el código de entrega proporcionado por la otra parte."
                        } else {
                            mensajeEstado = null
                            entregaRealizada = true
                            onConfirmarEntrega(codigoVerificacion)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Confirmar Entrega de Alimentos")
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(
                        text = "¡Entrega confirmada e impactada en los indicadores del sistema!",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
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