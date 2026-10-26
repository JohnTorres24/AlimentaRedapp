package com.example.alimentaredapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alimentaredapp.dto.PublicarLoteRequestDto
import com.example.alimentaredapp.ViewModel.LoteUiState
import com.example.alimentaredapp.ViewModel.LoteViewModel

@Composable
fun PublicarLoteScreen(
    token: String,
    viewModel: LoteViewModel = viewModel(),
    onPublicacionExitosa: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var unidadMedida by remember { mutableStateOf("") }
    var fechaExpiracion by remember { mutableStateOf("") }
    var categoriaId by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Publicar Alimento", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = titulo, onValueChange = { titulo = it }, label = { Text("Título (Ej. Manzanas)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = cantidad, onValueChange = { cantidad = it }, label = { Text("Cantidad Total") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = unidadMedida, onValueChange = { unidadMedida = it }, label = { Text("Unidad (Ej. KG, UNIDADES)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = fechaExpiracion, onValueChange = { fechaExpiracion = it }, label = { Text("Expiración (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = categoriaId, onValueChange = { categoriaId = it }, label = { Text("ID Categoría (Ej. 1)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        when (val state = uiState) {
            is LoteUiState.Loading -> CircularProgressIndicator()
            is LoteUiState.SuccessPublicar -> {
                Text(state.mensaje, color = MaterialTheme.colorScheme.primary)
                LaunchedEffect(Unit) { onPublicacionExitosa() }
            }
            is LoteUiState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error)
            else -> {}
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val dto = PublicarLoteRequestDto(
                    titulo = titulo,
                    descripcion = descripcion,
                    cantidadTotal = cantidad.toDoubleOrNull() ?: 0.0,
                    unidadMedida = unidadMedida,
                    fechaExpiracion = "${fechaExpiracion}T00:00:00", // Adaptación básica de formato fecha
                    categoriaId = categoriaId.toLongOrNull() ?: 1L
                )
                viewModel.publicarLote(token, dto)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is LoteUiState.Loading && titulo.isNotEmpty() && cantidad.isNotEmpty()
        ) {
            Text("Publicar")
        }
    }
}