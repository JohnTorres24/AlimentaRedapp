package com.example.alimentaredapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alimentaredapp.dto.RegistroOrganizacionRequestDto
import com.example.alimentaredapp.ViewModel.OrganizacionUiState
import com.example.alimentaredapp.ViewModel.OrganizacionViewModel

@Composable
fun RegistroOrganizacionScreen(
    viewModel: OrganizacionViewModel = viewModel(),
    onRegistroExitoso: () -> Unit,
    onIrAlLogin: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nitIdentificacion by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var latitud by remember { mutableStateOf("") }
    var longitud by remember { mutableStateOf("") }
    var tipoOrganizacion by remember { mutableStateOf("RECEPTOR") } // Valor por defecto

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crear Cuenta", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre de la Organización") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo electrónico") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = nitIdentificacion, onValueChange = { nitIdentificacion = it }, label = { Text("NIT / Identificación") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = latitud, onValueChange = { latitud = it }, label = { Text("Latitud (Ej. 4.6097)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = longitud, onValueChange = { longitud = it }, label = { Text("Longitud (Ej. -74.0817)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = tipoOrganizacion, onValueChange = { tipoOrganizacion = it.uppercase() }, label = { Text("Tipo (DONANTE o RECEPTOR)") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = uiState) {
            is OrganizacionUiState.Loading -> CircularProgressIndicator()
            is OrganizacionUiState.Success -> {
                Text(state.data.mensaje, color = MaterialTheme.colorScheme.primary)
                LaunchedEffect(Unit) { onRegistroExitoso() }
            }
            is OrganizacionUiState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error)
            else -> {}
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val dto = RegistroOrganizacionRequestDto(
                    nombre = nombre, email = email, passwordHash = password, nitIdentificacion = nitIdentificacion,
                    direccion = direccion, latitud = latitud.toDoubleOrNull() ?: 0.0, longitud = longitud.toDoubleOrNull() ?: 0.0,
                    tipoOrganizacion = tipoOrganizacion
                )
                viewModel.registrarOrganizacion(dto)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is OrganizacionUiState.Loading
        ) {
            Text("Registrarse")
        }

        TextButton(onClick = onIrAlLogin) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}