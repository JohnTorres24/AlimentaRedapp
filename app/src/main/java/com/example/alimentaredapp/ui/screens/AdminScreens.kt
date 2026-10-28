package com.example.alimentaredapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --- Flujo Completo de Administración con Bottom Navigation ---
@Composable
fun AdminFlowScreen(
    onVolver: () -> Unit
) {
    var adminTab by remember { mutableStateOf("resumen") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    fun mostrarMensaje(mensaje: String) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(mensaje)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Surface(
                tonalElevation = 4.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 4.dp
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.GridView, contentDescription = "Resumen") },
                        label = { Text("Resumen") },
                        selected = adminTab == "resumen",
                        onClick = { adminTab = "resumen" }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.People, contentDescription = "Usuarios") },
                        label = { Text("Usuarios") },
                        selected = adminTab == "usuarios",
                        onClick = { adminTab = "usuarios" }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Tune, contentDescription = "Gestión") },
                        label = { Text("Gestión") },
                        selected = adminTab == "gestion",
                        onClick = { adminTab = "gestion" }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = "Auditoría") },
                        label = { Text("Auditoría") },
                        selected = adminTab == "auditoria",
                        onClick = { adminTab = "auditoria" }
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (adminTab) {
                "resumen" -> AdminDashboardScreen(
                    onVolver = onVolver,
                    onNavigateToTab = { tab -> adminTab = tab }
                )
                "usuarios" -> AdminUsuariosScreen(
                    onVolver = onVolver,
                    onMostrarMensaje = { mostrarMensaje(it) }
                )
                "gestion" -> AdminGestionTabsScreen(
                    onVolver = onVolver,
                    onMostrarMensaje = { mostrarMensaje(it) }
                )
                "auditoria" -> AdminAuditoriaScreen(
                    onVolver = onVolver
                )
            }
        }
    }
}

// 1. PANTALLA: Admin - Dashboard (Resumen)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onVolver: () -> Unit,
    onNavigateToTab: (String) -> Unit
) {
    var mostrarAlertasDialog by remember { mutableStateOf(false) }
    var mostrarImpactoDialog by remember { mutableStateOf(false) }

    if (mostrarAlertasDialog) {
        AlertDialog(
            onDismissRequest = { mostrarAlertasDialog = false },
            icon = { Icon(Icons.Default.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            title = { Text("Notificaciones del Sistema", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("• Incidente I-0073: Embalaje deteriorado reportado en la entrega E-1042. Requiere revisión técnica.", fontSize = 13.sp)
                    Text("• Seguridad: Regla de margen mínimo de 4 horas activa y sin anomalías.", fontSize = 13.sp)
                    Text("• Sincronización: Conexión con el servidor establecida correctamente.", fontSize = 13.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { mostrarAlertasDialog = false }) {
                    Text("Entendido")
                }
            }
        )
    }

    if (mostrarImpactoDialog) {
        AlertDialog(
            onDismissRequest = { mostrarImpactoDialog = false },
            icon = { Icon(Icons.Default.TrendingUp, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            title = { Text("Resumen de Impacto de Octubre", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("• Total Alimentos Donados: 1.240 kg", fontWeight = FontWeight.SemiBold)
                    Text("• Huella de Carbono Evitada: 3.100 kg CO₂eq")
                    Text("• Personas Beneficiadas Estimadas: 2.480")
                    Text("• Eficiencia de Recogida: 94.8%")
                }
            },
            confirmButton = {
                TextButton(onClick = { mostrarImpactoDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Spa, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Panel de administración", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text("Administrador del Sistema · AlimentaRed", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver a la App")
                    }
                },
                actions = {
                    IconButton(onClick = { mostrarAlertasDialog = true }) {
                        Icon(Icons.Default.NotificationsNone, contentDescription = "Alertas")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Una red que funciona", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            // Cuadrícula 2x2 de Estadísticas (Todas interactivas)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminStatCard(
                    valor = "48",
                    etiqueta = "Usuarios verificados",
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToTab("usuarios") }
                )
                AdminStatCard(
                    valor = "8",
                    etiqueta = "Lotes disponibles",
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToTab("gestion") }
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminStatCard(
                    valor = "1.240 kg",
                    etiqueta = "Donados en octubre",
                    modifier = Modifier
                        .weight(1f)
                        .clickable { mostrarImpactoDialog = true }
                )
                AdminStatCard(
                    valor = "36",
                    etiqueta = "Entregas completadas",
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToTab("auditoria") }
                )
            }

            // Alerta de Incidencia (Interactiva)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToTab("auditoria") },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFE65100))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("1 incidente requiere revisión", fontWeight = FontWeight.Bold, color = Color(0xFFE65100), fontSize = 14.sp)
                        Text("I-0073 · Embalaje deteriorado · 26 oct, 13:40", fontSize = 12.sp, color = Color(0xFF8D6E63))
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFFE65100))
                }
            }

            // Salud de la Plataforma
            Text("Salud de la plataforma", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Seguridad", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                        Text("Mínimo 4 h · Regla activa", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    }
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Solicitudes", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                        Text("Máximo 3 activas · Regla activa", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    }
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Última auditoría", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                        Text("Hoy, 08:15 · Sin anomalías", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    }
                }
            }

            OutlinedButton(
                onClick = { onNavigateToTab("auditoria") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Revisar actividad y auditoría", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun AdminStatCard(valor: String, etiqueta: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(valor, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(etiqueta, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// 2. PANTALLA: Admin - Gestión de Usuarios
data class AdminUsuarioModel(
    val id: String,
    val nombre: String,
    val tipo: String, // "Donante", "Organización", "Admin"
    val encargado: String,
    val email: String,
    val infoExtra: String,
    var activo: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsuariosScreen(
    onVolver: () -> Unit,
    onMostrarMensaje: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var filtroSeleccionado by remember { mutableStateOf("Todos") }
    var usuarioSeleccionado by remember { mutableStateOf<AdminUsuarioModel?>(null) }
    var mostrarMenuOpciones by remember { mutableStateOf(false) }

    val listaUsuarios = remember {
        mutableStateListOf(
            AdminUsuarioModel("D-031", "Mercado de Triana", "Donante", "Ana García", "ana@mercadotriana.com", "Donante activo · 12 donaciones realizadas", true),
            AdminUsuarioModel("O-018", "Red Vecinal Sevilla", "Organización", "Lucía Martín", "lucia@redvecinal.com", "2/3 solicitudes activas · Verificada", true),
            AdminUsuarioModel("O-021", "Asociación Puente Sur", "Organización", "Pedro López", "pedro@puentesur.com", "1/3 solicitudes activas · Verificada", true),
            AdminUsuarioModel("D-042", "Supermercados El Sol", "Donante", "Carlos Mendoza", "carlos@elsol.com", "Donante frecuente · 28 donaciones", true),
            AdminUsuarioModel("O-035", "Comedor San Cayetano", "Organización", "Marta Gómez", "contacto@sancayetano.org", "Organización receptora autorizada", true)
        )
    }

    // Filtrado interactivo en tiempo real
    val usuariosFiltrados = remember(query, filtroSeleccionado, listaUsuarios.toList()) {
        listaUsuarios.filter { usuario ->
            val cumpleFiltro = when (filtroSeleccionado) {
                "Donantes" -> usuario.tipo.equals("Donante", ignoreCase = true)
                "Organizaciones" -> usuario.tipo.equals("Organización", ignoreCase = true)
                "Inactivos" -> !usuario.activo
                else -> true
            }
            val cumpleQuery = query.isBlank() ||
                    usuario.nombre.contains(query, ignoreCase = true) ||
                    usuario.email.contains(query, ignoreCase = true) ||
                    usuario.id.contains(query, ignoreCase = true) ||
                    usuario.encargado.contains(query, ignoreCase = true)

            cumpleFiltro && cumpleQuery
        }
    }

    // Diálogo de Inspección y Cambio de Estado de Usuario
    usuarioSeleccionado?.let { usuario ->
        AlertDialog(
            onDismissRequest = { usuarioSeleccionado = null },
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            title = { Text(usuario.nombre, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Identificador: ${usuario.id}", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Text("Tipo: ${usuario.tipo}", fontSize = 13.sp)
                    Text("Contacto / Encargado: ${usuario.encargado}", fontSize = 13.sp)
                    Text("Correo: ${usuario.email}", fontSize = 13.sp)
                    Text("Detalle: ${usuario.infoExtra}", fontSize = 13.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Estado actual: ", fontSize = 13.sp)
                        Text(
                            text = if (usuario.activo) "ACTIVO / VERIFICADO" else "SUSPENDIDO / INACTIVO",
                            color = if (usuario.activo) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val index = listaUsuarios.indexOfFirst { it.id == usuario.id }
                        if (index != -1) {
                            val nuevoEstado = !usuario.activo
                            listaUsuarios[index] = usuario.copy(activo = nuevoEstado)
                            onMostrarMensaje("Usuario ${usuario.nombre} ${if (nuevoEstado) "reactivado" else "suspendido"} exitosamente")
                        }
                        usuarioSeleccionado = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (usuario.activo) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(if (usuario.activo) "Suspender Usuario" else "Reactivar Usuario")
                }
            },
            dismissButton = {
                TextButton(onClick = { usuarioSeleccionado = null }) {
                    Text("Cerrar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Gestión de usuarios", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("${listaUsuarios.size} usuarios registrados en la red", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { mostrarMenuOpciones = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
                        }
                        DropdownMenu(
                            expanded = mostrarMenuOpciones,
                            onDismissRequest = { mostrarMenuOpciones = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Mostrar todos") },
                                onClick = {
                                    filtroSeleccionado = "Todos"
                                    mostrarMenuOpciones = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Mostrar solo donantes") },
                                onClick = {
                                    filtroSeleccionado = "Donantes"
                                    mostrarMenuOpciones = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Mostrar solo organizaciones") },
                                onClick = {
                                    filtroSeleccionado = "Organizaciones"
                                    mostrarMenuOpciones = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Actualizar lista") },
                                onClick = {
                                    mostrarMenuOpciones = false
                                    onMostrarMensaje("Lista de usuarios actualizada")
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Buscar usuario") },
                placeholder = { Text("Nombre, correo o ID") },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpiar")
                        }
                    } else {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Todos", "Donantes", "Organizaciones", "Inactivos").forEach { chip ->
                    FilterChip(
                        selected = filtroSeleccionado == chip,
                        onClick = { filtroSeleccionado = chip },
                        label = { Text(chip) }
                    )
                }
            }

            if (usuariosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No se encontraron usuarios con el criterio actual.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(usuariosFiltrados, key = { it.id }) { usuario ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { usuarioSeleccionado = usuario },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (usuario.activo) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
                                    ) {
                                        Text(
                                            text = if (usuario.activo) "${usuario.tipo} · Activo" else "${usuario.tipo} · Suspendido",
                                            fontSize = 12.sp,
                                            color = if (usuario.activo) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(usuario.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("${usuario.id} · ${usuario.encargado}", fontSize = 13.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
                                Text(usuario.email, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(usuario.infoExtra, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            // NOTA: El botón "Invitar usuario" fue eliminado de acuerdo a los requerimientos del usuario.
        }
    }
}

// 3. PANTALLA: Gestión (Categorías + Reglas)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminGestionTabsScreen(
    onVolver: () -> Unit,
    onMostrarMensaje: (String) -> Unit
) {
    var subTab by remember { mutableStateOf("categorias") }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Gestión de la Plataforma", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onVolver) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
        )

        PrimaryTabRow(
            selectedTabIndex = if (subTab == "categorias") 0 else 1
        ) {
            Tab(
                selected = subTab == "categorias",
                onClick = { subTab = "categorias" },
                text = { Text("Categorías") },
                icon = { Icon(Icons.Default.Category, contentDescription = null) }
            )
            Tab(
                selected = subTab == "reglas",
                onClick = { subTab = "reglas" },
                text = { Text("Reglas de Plataforma") },
                icon = { Icon(Icons.Default.Tune, contentDescription = null) }
            )
        }

        if (subTab == "categorias") {
            AdminCategoriasScreen(onMostrarMensaje = onMostrarMensaje)
        } else {
            AdminConfiguracionScreen(onMostrarMensaje = onMostrarMensaje)
        }
    }
}

data class CategoriaItemModel(
    val nombre: String,
    var descripcion: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCategoriasScreen(
    onMostrarMensaje: (String) -> Unit
) {
    val categorias = remember {
        mutableStateListOf(
            CategoriaItemModel("Verduras", "3 lotes disponibles"),
            CategoriaItemModel("Fruta", "2 lotes disponibles"),
            CategoriaItemModel("Panadería", "1 lote disponible"),
            CategoriaItemModel("Despensa", "2 lotes disponibles"),
            CategoriaItemModel("Lácteos", "Sin lotes disponibles"),
            CategoriaItemModel("Comida preparada", "Sin lotes disponibles")
        )
    }

    var mostrarCrearDialog by remember { mutableStateOf(false) }
    var nuevoNombreCategoria by remember { mutableStateOf("") }
    var nuevaDescripcionCategoria by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf<CategoriaItemModel?>(null) }

    // Diálogo para Añadir Nueva Categoría
    if (mostrarCrearDialog) {
        AlertDialog(
            onDismissRequest = {
                mostrarCrearDialog = false
                nuevoNombreCategoria = ""
                nuevaDescripcionCategoria = ""
            },
            icon = { Icon(Icons.Default.AddCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            title = { Text("Añadir Nueva Categoría", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = nuevoNombreCategoria,
                        onValueChange = { nuevoNombreCategoria = it },
                        label = { Text("Nombre de la categoría") },
                        placeholder = { Text("Ej: Granos y Legumbres") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = nuevaDescripcionCategoria,
                        onValueChange = { nuevaDescripcionCategoria = it },
                        label = { Text("Descripción o notas") },
                        placeholder = { Text("Ej: Lotes de legumbres, arroz, cereales") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val nombreLimpio = nuevoNombreCategoria.trim()
                        if (nombreLimpio.isNotBlank()) {
                            categorias.add(
                                CategoriaItemModel(
                                    nombre = nombreLimpio,
                                    descripcion = if (nuevaDescripcionCategoria.isBlank()) "Sin lotes disponibles" else nuevaDescripcionCategoria.trim()
                                )
                            )
                            onMostrarMensaje("Categoría '$nombreLimpio' añadida exitosamente")
                            mostrarCrearDialog = false
                            nuevoNombreCategoria = ""
                            nuevaDescripcionCategoria = ""
                        }
                    },
                    enabled = nuevoNombreCategoria.trim().isNotBlank()
                ) {
                    Text("Crear Categoría")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarCrearDialog = false
                    nuevoNombreCategoria = ""
                    nuevaDescripcionCategoria = ""
                }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo para ver o editar categoría existente
    categoriaSeleccionada?.let { cat ->
        AlertDialog(
            onDismissRequest = { categoriaSeleccionada = null },
            icon = { Icon(Icons.Default.Category, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            title = { Text(cat.nombre, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Estado actual en catálogo: ${cat.descripcion}", fontSize = 14.sp)
                    Text("Esta categoría está disponible para todos los donantes de alimentos.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            confirmButton = {
                TextButton(onClick = { categoriaSeleccionada = null }) {
                    Text("Aceptar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Categorías de alimentos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text("${categorias.size} categorías activas · Catálogo compartido", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categorias) { cat ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { categoriaSeleccionada = cat },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(cat.nombre, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(cat.descripcion, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        // Botón Funcional de Añadir Categoría
        Button(
            onClick = { mostrarCrearDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Añadir categoría", fontWeight = FontWeight.Bold)
        }
        Text("Los cambios se registran en la auditoría inmutable del sistema.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// 4. PANTALLA: Admin - Configuración de Reglas
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminConfiguracionScreen(
    onMostrarMensaje: (String) -> Unit
) {
    var margenSeguridadHoras by remember { mutableIntStateOf(4) }
    var maxSolicitudesActivas by remember { mutableIntStateOf(3) }
    var ultimaRevisionTexto by remember {
        mutableStateOf("Hoy, ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())} · Administrador")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Reglas de la plataforma", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text("Configuración global del sistema", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

        // Seguridad Alimentaria
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Seguridad alimentaria", fontWeight = FontWeight.Bold)
                Text("Margen mínimo obligatorio antes del vencimiento", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("$margenSeguridadHoras horas mínimas", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                    Row {
                        IconButton(
                            onClick = { if (margenSeguridadHoras > 2) margenSeguridadHoras -= 1 },
                            enabled = margenSeguridadHoras > 2
                        ) {
                            Icon(Icons.Default.RemoveCircleOutline, contentDescription = "Disminuir")
                        }
                        IconButton(
                            onClick = { if (margenSeguridadHoras < 24) margenSeguridadHoras += 1 },
                            enabled = margenSeguridadHoras < 24
                        ) {
                            Icon(Icons.Default.AddCircleOutline, contentDescription = "Aumentar")
                        }
                    }
                }

                Text("Caducidad - fin de recogida >= $margenSeguridadHoras h. Menos de este margen bloquea la publicación del lote.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        // Solicitudes por Organización
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Solicitudes por organización", fontWeight = FontWeight.Bold)
                Text("Máximo de solicitudes activas simultáneas", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("$maxSolicitudesActivas solicitudes", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                    Row {
                        IconButton(
                            onClick = { if (maxSolicitudesActivas > 1) maxSolicitudesActivas -= 1 },
                            enabled = maxSolicitudesActivas > 1
                        ) {
                            Icon(Icons.Default.RemoveCircleOutline, contentDescription = "Disminuir")
                        }
                        IconButton(
                            onClick = { if (maxSolicitudesActivas < 10) maxSolicitudesActivas += 1 },
                            enabled = maxSolicitudesActivas < 10
                        ) {
                            Icon(Icons.Default.AddCircleOutline, contentDescription = "Aumentar")
                        }
                    }
                }

                Text("Incluye solicitudes pendientes, aprobadas y en tránsito. Con $maxSolicitudesActivas/$maxSolicitudesActivas se bloquea la solicitud de nuevos lotes para asegurar distribución equitativa.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        // Reglas Protegidas
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Security, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Reglas obligatorias protegidas", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("Estos límites garantizan la integridad de la red y no pueden anularse.", fontSize = 12.sp)
                }
            }
        }

        Text("Última revisión: $ultimaRevisionTexto", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

        // Botón Funcional de Guardar Configuración
        Button(
            onClick = {
                val horaActual = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                ultimaRevisionTexto = "Hoy, $horaActual · Administrador"
                onMostrarMensaje("¡Reglas actualizadas! Margen: $margenSeguridadHoras h | Solicitudes máx: $maxSolicitudesActivas")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Default.Save, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Guardar configuración", fontWeight = FontWeight.Bold)
        }
    }
}

// 5. PANTALLA: Admin - Auditoría
data class AuditLogModel(
    val id: String,
    val evento: String,
    val codigos: String,
    val detalle: String,
    val fecha: String,
    val hashIntegridad: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAuditoriaScreen(onVolver: () -> Unit) {
    var query by remember { mutableStateOf("") }
    var filtroFecha by remember { mutableStateOf("Todos") }
    var logSeleccionado by remember { mutableStateOf<AuditLogModel?>(null) }

    val logs = remember {
        listOf(
            AuditLogModel("EV-9015", "Recepción confirmada", "EV-9015 · S-2083 · L-1042", "Lucía Martín · O-018 | 28/10/2026 · 12:25", "Hoy", "SHA-256: 4a8f9c...02e1 VÁLIDO"),
            AuditLogModel("EV-9014", "Recogida confirmada", "EV-9014 · S-2083 · L-1042", "Ana García · D-031 | 28/10/2026 · 12:10", "Hoy", "SHA-256: 9b2d1f...88c3 VÁLIDO"),
            AuditLogModel("EV-9013", "Solicitud aprobada", "EV-9013 · S-2083 · L-1042", "Ana García · D-031 | 28/10/2026 · 10:15", "Hoy", "SHA-256: 3c7a5e...12a9 VÁLIDO"),
            AuditLogModel("EV-9012", "Lote publicado", "EV-9012 · L-1042", "Mercado de Triana · D-031 | 27/10/2026 · 18:00", "Ayer", "SHA-256: 7d1e8a...55bf VÁLIDO"),
            AuditLogModel("EV-9011", "Incidencia resuelta", "EV-9011 · I-0072", "Admin Sistema | 27/10/2026 · 16:30", "Ayer", "SHA-256: 1a9f3b...44dd VÁLIDO")
        )
    }

    val logsFiltrados = remember(query, filtroFecha) {
        logs.filter { log ->
            val cumpleFiltro = when (filtroFecha) {
                "Hoy" -> log.fecha.equals("Hoy", ignoreCase = true)
                "Ayer" -> log.fecha.equals("Ayer", ignoreCase = true)
                else -> true
            }
            val cumpleQuery = query.isBlank() ||
                    log.evento.contains(query, ignoreCase = true) ||
                    log.codigos.contains(query, ignoreCase = true) ||
                    log.detalle.contains(query, ignoreCase = true)

            cumpleFiltro && cumpleQuery
        }
    }

    // Diálogo de Inspección de Auditoría
    logSeleccionado?.let { log ->
        AlertDialog(
            onDismissRequest = { logSeleccionado = null },
            icon = { Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            title = { Text(log.evento, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Identificador: ${log.id}", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Text("Trazabilidad: ${log.codigos}", fontSize = 13.sp)
                    Text("Detalles: ${log.detalle}", fontSize = 13.sp)
                    Text("Integridad Criptográfica: ${log.hashIntegridad}", fontSize = 12.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                    Text("Este registro es inmutable y forma parte del historial oficial del sistema.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            confirmButton = {
                TextButton(onClick = { logSeleccionado = null }) {
                    Text("Cerrar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Registro de auditoría", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Eventos inmutables y trazabilidad", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.FilterList, contentDescription = "Filtros") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Buscar en el registro") },
                placeholder = { Text("Evento, actor o ID de entidad") },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpiar")
                        }
                    } else {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Todos", "Hoy", "Ayer").forEach { chip ->
                    FilterChip(
                        selected = filtroFecha == chip,
                        onClick = { filtroFecha = chip },
                        label = { Text(chip) }
                    )
                }
            }

            if (logsFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No se encontraron registros de auditoría.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(logsFiltrados) { log ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { logSeleccionado = log },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(log.evento, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(log.codigos, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(log.detalle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            // NOTA: El botón "Exportar registro" fue eliminado de acuerdo a los requerimientos del usuario.
        }
    }
}
