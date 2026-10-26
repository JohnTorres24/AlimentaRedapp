package com.example.alimentared.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

// Modelo de prueba temporal corregido con 'val'
data class LoteAlimentoItem(
    val id: Long,
    val titulo: String,
    val categoria: String,
    val cantidad: String,
    val fechaExpiracion: String,
    val donante: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LotesScreen(
    onLoteClick: (Long) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    // Lista de ejemplo simulando datos que luego vendrán del Backend de Spring Boot (/api/lotes)
    val lotesEjemplo = listOf(
        LoteAlimentoItem(1, "Panadería del Día", "Panadería", "15 kg", "2026-10-26", "Panadería La Española"),
        LoteAlimentoItem(2, "Verduras Frescas", "Frutas y Verduras", "30 kg", "2026-10-27", "Supermercado Ahal"),
        LoteAlimentoItem(3, "Lotes de Lácteos", "Lácteos", "10 Litros", "2026-10-28", "Cafetería Central")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AlimentaRed - Lotes Disponibles") },
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
        ) {
            // Barra de búsqueda / filtro por categoría o ubicación (RF04)[cite: 11]
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Filtrar por categoría o nombre...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ofertas activas para rescate",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Lista dinámica de lotes
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(lotesEjemplo) { lote ->
                    Card(
                        onClick = { onLoteClick(lote.id) },
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = lote.titulo,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Categoría: ${lote.categoria} | Cantidad: ${lote.cantidad}")
                            Text(text = "Expira: ${lote.fechaExpiracion}", fontSize = 12.sp, color = MaterialTheme.colorScheme.error)
                            Text(text = "Donante: ${lote.donante}", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                        }
                    }
                }
            }
        }
    }
}

