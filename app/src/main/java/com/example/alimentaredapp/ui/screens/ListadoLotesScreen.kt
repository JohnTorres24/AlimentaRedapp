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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
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

    LaunchedEffect(Unit) {
        viewModel.obtenerLotes(token)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "AlimentaRed",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Lotes Disponibles para Rescate",
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
                text = { Text("+ Donar Alimento", fontWeight = FontWeight.Bold) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Barra de búsqueda con icono (RF04)
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

            when (val state = uiState) {
                is LoteUiState.Loading, LoteUiState.Idle -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Consultando alimentos disponibles...", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
                is LoteUiState.SuccessConsultar -> {
                    // Filtro dinámico en tiempo real por búsqueda y categoría
                    val lotesFiltrados = state.lotes.filter { lote ->
                        val coincideTexto = searchQuery.isBlank() ||
                                lote.titulo.contains(searchQuery, ignoreCase = true) ||
                                (lote.nombreCategoria?.contains(searchQuery, ignoreCase = true) == true) ||
                                (lote.nombreOrganizacion?.contains(searchQuery, ignoreCase = true) == true)

                        val coincideCategoria = categoriaSeleccionada == "Todos" ||
                                (lote.nombreCategoria?.contains(categoriaSeleccionada, ignoreCase = true) == true)

                        coincideTexto && coincideCategoria
                    }

                    if (lotesFiltrados.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(54.dp), tint = MaterialTheme.colorScheme.outline)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("No se encontraron alimentos con estos filtros.", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Puedes publicar un nuevo lote o intentar con otra categoría.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = onPublicarClick, shape = RoundedCornerShape(10.dp)) {
                                Text("Publicar Donación Ahora")
                            }
                        }
                    } else {
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
                is LoteUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("No se pudo cargar la lista", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(state.message, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.obtenerLotes(token) }) {
                            Text("Reintentar Conexión")
                        }
                    }
                }
                else -> {}
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
                    "AGOTADO", "VENCIDO" -> StatusExpired
                    else -> MaterialTheme.colorScheme.primary
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = estadoColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = lote.estado,
                        color = estadoColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            if (!lote.nombreCategoria.isNullOrBlank()) {
                Text(
                    text = "Categoría: ${lote.nombreCategoria} | Cantidad: ${lote.cantidadDisponible} ${lote.unidadMedida}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            } else {
                Text(
                    text = "Cantidad: ${lote.cantidadDisponible} ${lote.unidadMedida}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccessTime, contentDescription = null, tint = StatusExpired, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Expira: ${lote.fechaExpiracion.replace("T", " ")}",
                    fontSize = 12.sp,
                    color = StatusExpired,
                    fontWeight = FontWeight.Medium
                )
            }

            if (!lote.nombreOrganizacion.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Store, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Donante: ${lote.nombreOrganizacion}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}
