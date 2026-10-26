package com.example.alimentaredapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alimentaredapp.ViewModel.EntregaUiState
import com.example.alimentaredapp.ViewModel.EntregaViewModel

@Composable
fun ConfirmarEntregaScreen(
    idEntrega: Long,
    token: String,
    viewModel: EntregaViewModel = viewModel(),
    onConfirmacionExitosa: () -> Unit
) {
    var responsable by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Confirmar Entrega #$idEntrega", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = responsable,
            onValueChange = { responsable = it },
            label = { Text("Nombre del Responsable") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = notas,
            onValueChange = { notas = it },
            label = { Text("Notas u observaciones (Opcional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = uiState) {
            is EntregaUiState.Loading -> CircularProgressIndicator()
            is EntregaUiState.Success -> {
                Text(state.data.mensaje, color = MaterialTheme.colorScheme.primary)
                LaunchedEffect(Unit) { onConfirmacionExitosa() }
            }
            is EntregaUiState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error)
            else -> {}
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.confirmarEntrega(token, idEntrega, responsable, notas)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is EntregaUiState.Loading && responsable.isNotEmpty()
        ) {
            Text("Confirmar Recepción")
        }
    }
}