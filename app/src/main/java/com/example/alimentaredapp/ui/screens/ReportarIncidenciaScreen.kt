package com.example.alimentaredapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alimentaredapp.ViewModel.IncidenciaUiState
import com.example.alimentaredapp.ViewModel.IncidenciaViewModel

@Composable
fun ReportarIncidenciaScreen(
    idEntrega: Long,
    token: String,
    viewModel: IncidenciaViewModel = viewModel(),
    onReporteExitoso: () -> Unit
) {
    var motivo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Reportar Problema (#$idEntrega)", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = motivo,
            onValueChange = { motivo = it },
            label = { Text("Motivo (Ej. Alimentos en mal estado)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción detallada") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3 // Hace la caja de texto más grande
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = uiState) {
            is IncidenciaUiState.Loading -> CircularProgressIndicator()
            is IncidenciaUiState.Success -> {
                Text(state.data.mensaje, color = MaterialTheme.colorScheme.primary)
                LaunchedEffect(Unit) { onReporteExitoso() }
            }
            is IncidenciaUiState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error)
            else -> {}
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.reportarIncidencia(token, idEntrega, motivo, descripcion)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is IncidenciaUiState.Loading && motivo.isNotEmpty() && descripcion.isNotEmpty()
        ) {
            Text("Enviar Reporte")
        }
    }
}