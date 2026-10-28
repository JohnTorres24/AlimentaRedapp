package com.example.alimentaredapp.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alimentaredapp.dto.RegistroOrganizacionRequestDto
import com.example.alimentaredapp.util.LocationHelper
import com.example.alimentaredapp.ViewModel.OrganizacionUiState
import com.example.alimentaredapp.ViewModel.OrganizacionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroOrganizacionScreen(
    viewModel: OrganizacionViewModel = viewModel(),
    onRegistroExitoso: () -> Unit,
    onIrAlLogin: () -> Unit
) {
    val context = LocalContext.current
    val direccionAuto by LocationHelper.direccionDetectada.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nitIdentificacion by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf(direccionAuto) }
    var tipoOrganizacion by remember { mutableStateOf("RECEPTOR") }
    var expandedDropdown by remember { mutableStateOf(false) }

    // Sincronizar dirección detectada automáticamente si el usuario no la ha cambiado
    LaunchedEffect(direccionAuto) {
        if (direccion.isBlank() || direccion == "Cajicá, Cundinamarca") {
            direccion = direccionAuto
        }
    }

    val tiposDisponibles = listOf(
        "RECEPTOR" to "Organización Receptora (Comedor / Fundación)",
        "DONANTE" to "Donante de Alimentos (Restaurante / Empresa)"
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val concedido = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (concedido) {
            LocationHelper.obtenerUbicacionLegible(context)
        }
    }

    fun solicitarUbicacion() {
        val fineGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarseGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {
            LocationHelper.obtenerUbicacionLegible(context)
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AlimentaRed - Registro") },
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
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Crea tu cuenta institucional en la red",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            // Menú Desplegable: Tipo de Actor / Organización
            Text(
                text = "Tipo de Actor",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expandedDropdown,
                onExpandedChange = { expandedDropdown = !expandedDropdown },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = tiposDisponibles.firstOrNull { it.first == tipoOrganizacion }?.second ?: tipoOrganizacion,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Selecciona si eres Receptor o Donante") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true),
                    shape = RoundedCornerShape(10.dp)
                )

                ExposedDropdownMenu(
                    expanded = expandedDropdown,
                    onDismissRequest = { expandedDropdown = false }
                ) {
                    tiposDisponibles.forEach { (codigo, descripcion) ->
                        DropdownMenuItem(
                            text = { Text(descripcion) },
                            onClick = {
                                tipoOrganizacion = codigo
                                expandedDropdown = false
                            }
                        )
                    }
                }
            }

            // Nombre / Razón Social
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre de la Organización / Razón Social") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            // NIT / Identificación
            OutlinedTextField(
                value = nitIdentificacion,
                onValueChange = { nitIdentificacion = it },
                label = { Text("NIT o Documento de Identificación") },
                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            // Correo Electrónico
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico Institucional") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            // Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            // Campo de Ubicación / Dirección (en formato legible de ciudad/departamento)
            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Ubicación / Dirección (ej. Cajicá, Cundinamarca)") },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { solicitarUbicacion() }) {
                        Icon(Icons.Default.MyLocation, contentDescription = "Detectar Ubicación", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            // Manejo de Estados de Red
            when (val state = uiState) {
                is OrganizacionUiState.Loading -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Registrando organización en Render...", fontSize = 14.sp)
                    }
                }
                is OrganizacionUiState.Success -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "¡Organización registrada exitosamente!",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(12.dp),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    LaunchedEffect(Unit) {
                        onRegistroExitoso()
                    }
                }
                is OrganizacionUiState.Error -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = state.message,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Botón de Registro
            val formularioValido = nombre.isNotBlank() &&
                    email.isNotBlank() &&
                    password.isNotBlank() &&
                    nitIdentificacion.isNotBlank() &&
                    direccion.isNotBlank()

            Button(
                onClick = {
                    val request = RegistroOrganizacionRequestDto(
                        nombre = nombre.trim(),
                        email = email.trim(),
                        passwordHash = password,
                        nitIdentificacion = nitIdentificacion.trim(),
                        direccion = direccion.trim(),
                        latitud = 0.0,
                        longitud = 0.0,
                        tipoOrganizacion = tipoOrganizacion
                    )
                    viewModel.registrarOrganizacion(request)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = uiState !is OrganizacionUiState.Loading && formularioValido,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Registrar Organización", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            // Enlace hacia Login
            TextButton(onClick = onIrAlLogin) {
                Text("¿Ya tienes una cuenta registrada? Inicia sesión aquí")
            }
        }
    }
}
