package com.example.alimentaredapp.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alimentaredapp.dto.RegistroOrganizacionRequestDto
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
    val locationManager = remember { context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager }

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nitIdentificacion by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var latitud by remember { mutableStateOf("") }
    var longitud by remember { mutableStateOf("") }
    var tipoOrganizacion by remember { mutableStateOf("RECEPTOR") }
    var expandedDropdown by remember { mutableStateOf(false) }
    var estadoUbicacion by remember { mutableStateOf<String?>(null) }

    val tiposDisponibles = listOf(
        "RECEPTOR" to "Organización Receptora (Comedor / Fundación)",
        "DONANTE" to "Donante de Alimentos (Restaurante / Empresa)"
    )

    // Función para obtener la última ubicación conocida
    @SuppressLint("MissingPermission")
    fun capturarUbicacion() {
        val providers = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER, LocationManager.PASSIVE_PROVIDER)
        var bestLocation: Location? = null
        for (provider in providers) {
            try {
                if (locationManager?.isProviderEnabled(provider) == true) {
                    val loc = locationManager.getLastKnownLocation(provider)
                    if (loc != null && (bestLocation == null || loc.accuracy < bestLocation.accuracy)) {
                        bestLocation = loc
                    }
                }
            } catch (_: Exception) {}
        }

        if (bestLocation != null) {
            latitud = String.format(java.util.Locale.US, "%.6f", bestLocation.latitude)
            longitud = String.format(java.util.Locale.US, "%.6f", bestLocation.longitude)
            estadoUbicacion = "Ubicación detectada exitosamente."
        } else {
            // Si el GPS no tiene última posición (común en emuladores recién iniciados), asignamos coordenadas de referencia
            if (latitud.isBlank()) latitud = "4.609710"
            if (longitud.isBlank()) longitud = "-74.081750"
            estadoUbicacion = "GPS sin señal reciente. Se asignó ubicación por defecto; puedes editarla."
        }
    }

    // Solicitud de permisos de ubicación en tiempo de ejecución
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val concedido = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (concedido) {
            capturarUbicacion()
        } else {
            estadoUbicacion = "Permiso de ubicación denegado. Ingresa latitud y longitud manualmente."
        }
    }

    fun solicitarUbicacion() {
        val fineGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarseGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {
            capturarUbicacion()
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crear Cuenta", style = MaterialTheme.typography.headlineMedium)
        Text(
            text = "Registro en AlimentaRed",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre de la Organización") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = nitIdentificacion,
            onValueChange = { nitIdentificacion = it },
            label = { Text("NIT / Identificación Fiscal") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Dirección física") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        // --- MENÚ DESPLEGABLE: RECEPTOR O DONANTE ---
        ExposedDropdownMenuBox(
            expanded = expandedDropdown,
            onExpandedChange = { expandedDropdown = !expandedDropdown },
            modifier = Modifier.fillMaxWidth()
        ) {
            val etiquetaSeleccionada = tiposDisponibles.firstOrNull { it.first == tipoOrganizacion }?.second ?: tipoOrganizacion
            OutlinedTextField(
                value = etiquetaSeleccionada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de Actor (Rol)") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expandedDropdown,
                onDismissRequest = { expandedDropdown = false }
            ) {
                tiposDisponibles.forEach { (codigo, descripcion) ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(codigo, style = MaterialTheme.typography.titleSmall)
                                Text(descripcion, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                            }
                        },
                        onClick = {
                            tipoOrganizacion = codigo
                            expandedDropdown = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- SECCIÓN DE UBICACIÓN AUTOMÁTICA POR GPS ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Ubicación", tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Geolocalización", style = MaterialTheme.typography.titleSmall)
                    }

                    FilledTonalButton(
                        onClick = { solicitarUbicacion() },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.MyLocation, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Detectar GPS")
                    }
                }

                estadoUbicacion?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = latitud,
                        onValueChange = { latitud = it },
                        label = { Text("Latitud") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = longitud,
                        onValueChange = { longitud = it },
                        label = { Text("Longitud") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = uiState) {
            is OrganizacionUiState.Loading -> {
                CircularProgressIndicator()
                Text("Registrando organización en AlimentaRed...", style = MaterialTheme.typography.bodySmall)
            }
            is OrganizacionUiState.Success -> {
                Text(state.data.mensaje, color = MaterialTheme.colorScheme.primary)
                LaunchedEffect(Unit) { onRegistroExitoso() }
            }
            is OrganizacionUiState.Error -> {
                Text(state.message, color = MaterialTheme.colorScheme.error)
            }
            else -> {}
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val dto = RegistroOrganizacionRequestDto(
                    nombre = nombre,
                    email = email,
                    passwordHash = password,
                    nitIdentificacion = nitIdentificacion,
                    direccion = direccion,
                    latitud = latitud.toDoubleOrNull() ?: 0.0,
                    longitud = longitud.toDoubleOrNull() ?: 0.0,
                    tipoOrganizacion = tipoOrganizacion
                )
                viewModel.registrarOrganizacion(dto)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is OrganizacionUiState.Loading && nombre.isNotBlank() && email.isNotBlank() && password.isNotBlank()
        ) {
            Text("Registrarse")
        }

        TextButton(onClick = onIrAlLogin) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}
