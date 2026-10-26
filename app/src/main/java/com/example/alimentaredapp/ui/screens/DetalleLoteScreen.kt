package com.example.alimentaredapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alimentaredapp.ViewModel.SolicitudUiState
import com.example.alimentaredapp.ViewModel.SolicitudViewModel

@Composable
fun DetalleLoteScreen(
    loteId: Long,
    token: String, // El token obtenido en el login
    viewModel: SolicitudViewModel = viewModel(),
    onReservaExitosa: () -> Unit
) {
    var cantidad by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }

    // Observa el estado del ViewModel (LotesUiState.Loading / Success)[cite: 7]
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Detalle del Lote", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = cantidad,
            onValueChange = { cantidad = it },
            label = { Text("Cantidad a reservar") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = observaciones,
            onValueChange = { observaciones = it },
            label = { Text("Observaciones (opcional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Muestra u oculta elementos según la respuesta del backend
        when (val state = uiState) {
            is SolicitudUiState.Loading -> {
                CircularProgressIndicator()
            }
            is SolicitudUiState.Success -> {
                Text(state.mensaje, color = MaterialTheme.colorScheme.primary)
                // Redirige o muestra mensaje de confirmación
                LaunchedEffect(Unit) {
                    onReservaExitosa()
                }
            }
            is SolicitudUiState.Error -> {
                Text(state.error, color = MaterialTheme.colorScheme.error)
            }
            else -> {}
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón "Solicitar Reserva" definido en el diagrama de secuencia[cite: 7]
        Button(
            onClick = {
                val cantidadDouble = cantidad.toDoubleOrNull() ?: 0.0
                viewModel.solicitarReserva(token, loteId, cantidadDouble, observaciones)
            },
            enabled = uiState !is SolicitudUiState.Loading && cantidad.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Solicitar Reserva")
        }
    }
}