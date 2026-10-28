package com.example.alimentaredapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearLoteScreen(
    onGuardarLote: (String, String, String, String, String) -> Unit,
    onVolver: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var cantidadTotal by remember { mutableStateOf("") }
    var unidadMedida by remember { mutableStateOf("kg") }
    var fechaExpiracion by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Publicar Lote de Alimentos") },
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
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Categoría
            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoría (ej. Panadería, Lácteos, Verduras)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Cantidad Total
            OutlinedTextField(
                value = cantidadTotal,
                onValueChange = { cantidadTotal = it },
                label = { Text("Cantidad total (ej. 15.5)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Unidad de Medida
            OutlinedTextField(
                value = unidadMedida,
                onValueChange = { unidadMedida = it },
                label = { Text("Unidad de medida (kg, litros, unidades)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Fecha Límite / Expiración
            OutlinedTextField(
                value = fechaExpiracion,
                onValueChange = { fechaExpiracion = it },
                label = { Text("Fecha límite (YYYY-MM-DD HH:mm)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (mensajeError != null) {
                Text(
                    text = mensajeError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón para Publicar / Guardar
            Button(
                onClick = {
                    if (titulo.isBlank() || categoria.isBlank() || cantidadTotal.isBlank() || fechaExpiracion.isBlank()) {
                        mensajeError = "Por favor completa todos los campos obligatorios."
                    } else {
                        mensajeError = null
                        // Llamada para enviar al backend (POST /api/lotes)
                        onGuardarLote(titulo, categoria, cantidadTotal, unidadMedida, fechaExpiracion)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Publicar Lote en la Red")
            }

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}

