package com.example.alimentaredapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alimentaredapp.dto.LoteResponseDto
import com.example.alimentaredapp.ViewModel.LoteUiState
import com.example.alimentaredapp.ViewModel.LoteViewModel

@Composable
fun ListadoLotesScreen(
    token: String,
    viewModel: LoteViewModel = viewModel(),
    onLoteClick: (Long) -> Unit // Acción al tocar un lote específico
) {
    val uiState by viewModel.uiState.collectAsState()

    // Cargar los lotes automáticamente al abrir la pantalla
    LaunchedEffect(Unit) {
        viewModel.obtenerLotes(token)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Lotes Disponibles", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        when (val state = uiState) {
            is LoteUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is LoteUiState.SuccessConsultar -> {
                val lotes = state.lotes
                if (lotes.isEmpty()) {
                    Text("No hay alimentos disponibles en este momento.")
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(lotes) { lote ->
                            LoteItemCard(lote = lote, onClick = { onLoteClick(lote.id) })
                        }
                    }
                }
            }
            is LoteUiState.Error -> {
                Text(state.message, color = MaterialTheme.colorScheme.error)
                Button(onClick = { viewModel.obtenerLotes(token) }, modifier = Modifier.padding(top = 8.dp)) {
                    Text("Reintentar")
                }
            }
            else -> {}
        }
    }
}

@Composable
fun LoteItemCard(lote: LoteResponseDto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = lote.titulo, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Cantidad: ${lote.cantidadDisponible} ${lote.unidadMedida}")
            Text(text = "Vence: ${lote.fechaExpiracion}")
        }
    }
}