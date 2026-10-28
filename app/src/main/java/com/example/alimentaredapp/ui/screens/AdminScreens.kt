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
import com.example.alimentaredapp.ui.theme.StatusAvailable

// --- Flujo Completo de Administración con Bottom Navigation ---
@Composable
fun AdminFlowScreen(
    onVolver: () -> Unit
) {
    var adminTab by remember { mutableStateOf("resumen") }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            when (adminTab) {
                "resumen" -> AdminDashboardScreen(
                    onRevisarActividad = { adminTab = "auditoria" }
                )
                "usuarios" -> AdminUsuariosScreen(
                    onVolver = onVolver
                )
                "gestion" -> AdminGestionTabsScreen(
                    onVolver = onVolver
                )
                "auditoria" -> AdminAuditoriaScreen(
                    onVolver = onVolver
                )
            }
        }

        // Barra inferior del Administrador (Resumen, Usuarios, Gestión, Auditoría)
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
}

// 1. PANTALLA: Admin - Dashboard (Resumen)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onRevisarActividad: () -> Unit
) {
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
                            Text("Marta Ruiz · Administradora · A-002", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
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

            // Cuadrícula 2x2 de Estadísticas
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminStatCard("48", "Usuarios verificados", modifier = Modifier.weight(1f))
                AdminStatCard("8", "Lotes disponibles", modifier = Modifier.weight(1f))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminStatCard("1.240 kg", "Donados en octubre", modifier = Modifier.weight(1f))
                AdminStatCard("36", "Entregas completadas", modifier = Modifier.weight(1f))
            }

            // Alerta de Incidencia
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFE65100))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("1 incidente requiere revisión", fontWeight = FontWeight.Bold, color = Color(0xFFE65100), fontSize = 14.sp)
                        Text("I-0073 · Embalaje deteriorado · 26 oct, 13:40", fontSize = 12.sp, color = Color(0xFF8D6E63))
                    }
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
                onClick = onRevisarActividad,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Revisar actividad", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsuariosScreen(
    onVolver: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    var filtroSeleccionado by remember { mutableStateOf("Todos") }

    val usuarios = remember {
        listOf(
            Triple("Mercado de Triana", "Donante · Activo", "D-031 · Ana García\nana@mercadotriana.com"),
            Triple("Red Vecinal Sevilla", "Organización · Activa", "O-018 · Lucía Martín\nlucia@redvecinal.com · 2/3 solicitudes activas"),
            Triple("Asociación Puente Sur", "Organización · Activa", "O-021 · Pedro López\npedro@puentesur.com · 1/3 solicitudes activas")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Gestión de usuarios", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("48 usuarios · 26 donantes · 20 organizaciones · 2 admins", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.MoreVert, contentDescription = "Más opciones") }
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
                trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Todos", "Donantes", "Organizaciones").forEach { chip ->
                    FilterChip(
                        selected = filtroSeleccionado == chip,
                        onClick = { filtroSeleccionado = chip },
                        label = { Text(chip) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(usuarios) { (nombre, estado, info) ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(estado, fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(info, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Invitar usuario", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 3. PANTALLA: Gestión (Categorías + Reglas)
@Composable
fun AdminGestionTabsScreen(
    onVolver: () -> Unit
) {
    var subTab by remember { mutableStateOf("categorias") }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = if (subTab == "categorias") 0 else 1) {
            Tab(selected = subTab == "categorias", onClick = { subTab = "categorias" }, text = { Text("Categorías") })
            Tab(selected = subTab == "reglas", onClick = { subTab = "reglas" }, text = { Text("Reglas de Plataforma") })
        }
        if (subTab == "categorias") {
            AdminCategoriasScreen(onVolver = onVolver)
        } else {
            AdminConfiguracionScreen(onVolver = onVolver)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCategoriasScreen(onVolver: () -> Unit) {
    val categorias = remember {
        listOf(
            "Verduras" to "3 lotes disponibles",
            "Fruta" to "2 lotes disponibles",
            "Panadería" to "1 lote disponible",
            "Despensa" to "2 lotes disponibles",
            "Lácteos" to "Sin lotes disponibles",
            "Comida preparada" to "Sin lotes disponibles"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Categorías de alimentos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text("6 categorías activas · Catálogo compartido", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categorias) { (cat, desc) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(cat, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(desc, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Añadir categoría", fontWeight = FontWeight.Bold)
        }
        Text("Los cambios se registran en la auditoría.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// 4. PANTALLA: Admin - Configuración de Reglas
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminConfiguracionScreen(onVolver: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Reglas de la plataforma", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text("Configuración global · Versión 1.4", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

        // Seguridad Alimentaria
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Seguridad alimentaria", fontWeight = FontWeight.Bold)
                Text("Margen mínimo obligatorio", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedTextField(
                    value = "4 horas",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
                Text("Caducidad - fin de recogida >= 4 h. Menos de 4 h bloquea la operación.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                Text("Máximo de solicitudes activas", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedTextField(
                    value = "3 solicitudes",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
                Text("Incluye pendientes, aprobadas y en tránsito. Con 3/3 no se admiten nuevas solicitudes.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                    Text("Estos límites no se pueden reducir ni desactivar.", fontSize = 12.sp)
                }
            }
        }

        Text("Última revisión: 28 oct 2026, 08:15 · Marta Ruiz", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Guardar configuración", fontWeight = FontWeight.Bold)
        }
    }
}

// 5. PANTALLA: Admin - Auditoría
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAuditoriaScreen(onVolver: () -> Unit) {
    var query by remember { mutableStateOf("") }
    var filtroFecha by remember { mutableStateOf("Hoy") }

    val logs = remember {
        listOf(
            Triple("Recepción confirmada", "EV-9015 · S-2083 · L-1042", "Lucía Martín · O-018 | 28/10/2026 · 12:25"),
            Triple("Recogida confirmada", "EV-9014 · S-2083 · L-1042", "Ana García · D-031 | 28/10/2026 · 12:10"),
            Triple("Solicitud aprobada", "EV-9013 · S-2083 · L-1042", "Ana García · D-031 | 28/10/2026 · 10:15")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Registro de auditoría", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Eventos inmutables · Cundinamarca", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.MoreVert, contentDescription = null) }
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
                trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Hoy", "Ayer", "Filtrar").forEach { chip ->
                    FilterChip(
                        selected = filtroFecha == chip,
                        onClick = { filtroFecha = chip },
                        label = { Text(chip) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(logs) { (evento, codigos, detalle) ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(evento, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(codigos, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(detalle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            OutlinedButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Download, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Exportar registro", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
