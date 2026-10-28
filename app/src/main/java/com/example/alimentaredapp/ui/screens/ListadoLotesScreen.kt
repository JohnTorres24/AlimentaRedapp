package com.example.alimentaredapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alimentaredapp.dto.LoteResponseDto
import com.example.alimentaredapp.ui.theme.StatusAvailable
import com.example.alimentaredapp.ui.theme.StatusExpired
import com.example.alimentaredapp.ui.theme.StatusReserved
import com.example.alimentaredapp.ViewModel.LoteUiState
import com.example.alimentaredapp.ViewModel.LoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListadoLotesScreen(
    token: String,
    viewModel: LoteViewModel = viewModel(),
    onLoteClick: (Long) -> Unit,
    onPublicarClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }

    val categoriasDisponibles = listOf("Todos", "Panadería", "Frutas y Verduras", "Lácteos", "Comidas Preparadas", "Granos")

    // Lotes representativos de los Mockups de Figma
    val lotesMockupBase = remember {
        listOf(
            LoteResponseDto(
                id = 1L,
                titulo = "Panadería Fresca del Día",
                descripcion = "Lote de pan fresco recién horneado",
                cantidadTotal = 15.0,
                cantidadDisponible = 15.0,
                unidadMedida = "kg",
                fechaExpiracion = "2026-10-28 12:00",
                nombreOrganizacion = "Panadería La Española",
                nombreCategoria = "Panadería",
                estado = "DISPONIBLE"
            ),
            LoteResponseDto(
                id = 2L,
                titulo = "Verduras Frescas Variadas",
                descripcion = "Verduras seleccionadas de huerta",
                cantidadTotal = 30.0,
                cantidadDisponible = 30.0,
                unidadMedida = "kg",
                fechaExpiracion = "2026-10-29 18:00",
                nombreOrganizacion = "Supermercado Ahal",
                nombreCategoria = "Frutas y Verduras",
                estado = "DISPONIBLE"
            ),
            LoteResponseDto(
                id = 3L,
                titulo = "Lotes de Lácteos y Quesos",
                descripcion = "Lácteos frescos pasteurizados",
                cantidadTotal = 10.0,
                cantidadDisponible = 10.0,
                unidadMedida = "Litros",
                fechaExpiracion = "2026-10-30 08:00",
                nombreOrganizacion = "Cafetería Central",
                nombreCategoria = "Lácteos",
                estado = "DISPONIBLE"
            )
        )
    }

    LaunchedEffect(token) {
        viewModel.obtenerLotes(token)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "AlimentaRed - Lotes Disponibles",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Rescate y redistribución de alimentos",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    IconButton(onClick = { viewModel.obtenerLotes(token) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Recargar", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onPublicarClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("+ Donar", fontWeight = FontWeight.Bold) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Barra de búsqueda con icono
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Filtrar por categoría o nombre...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Buscar", tint = MaterialTheme.colorScheme.primary)
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Chips Horizontales de Categorías
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categoriasDisponibles.forEach { cat ->
                    FilterChip(
                        selected = categoriaSeleccionada == cat,
                        onClick = { categoriaSeleccionada = cat },
                        label = { Text(cat) },
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Ofertas activas para rescate",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Determinación de la lista de lotes
            val listaBase = when (val state = uiState) {
                is LoteUiState.SuccessConsultar -> {
                    if (state.lotes.isNotEmpty()) state.lotes else lotesMockupBase
                }
                else -> lotesMockupBase
            }

            val lotesFiltrados = listaBase.filter { lote ->
                val coincideTexto = searchQuery.isBlank() ||
                        lote.titulo.contains(searchQuery, ignoreCase = true) ||
                        (lote.nombreCategoria?.contains(searchQuery, ignoreCase = true) == true) ||
                        (lote.nombreOrganizacion?.contains(searchQuery, ignoreCase = true) == true)

                val coincideCategoria = categoriaSeleccionada == "Todos" ||
                        (lote.nombreCategoria?.contains(categoriaSeleccionada, ignoreCase = true) == true)

                coincideTexto && coincideCategoria
            }

            if (uiState is LoteUiState.Loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp))
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(lotesFiltrados) { lote ->
                    LoteItemCard(lote = lote, onClick = { onLoteClick(lote.id) })
                }
            }
        }
    }
}

@Composable
fun LoteItemCard(lote: LoteResponseDto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = lote.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )

                val estadoColor = when (lote.estado.uppercase()) {
                    "DISPONIBLE" -> StatusAvailable
                    "RESERVADO" -> StatusReserved
                    else -> StatusExpired
                }

                Surface(
                    color = estadoColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = lote.estado.uppercase(),
                        color = estadoColor,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Categoría: ${lote.nombreCategoria ?: "Varios"} | Cantidad: ${lote.cantidadTotal} ${lote.unidadMedida}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Expira: ${lote.fechaExpiracion ?: "Pronto"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Store,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Donante: ${lote.nombreOrganizacion ?: "Red AlimentaRed"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
