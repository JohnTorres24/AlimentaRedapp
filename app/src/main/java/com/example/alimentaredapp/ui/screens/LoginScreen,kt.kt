package com.example.alimentaredapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alimentaredapp.Repository.RetrofitClient
import com.example.alimentaredapp.ViewModel.AuthUiState
import com.example.alimentaredapp.ViewModel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel = viewModel(),
    onLoginSuccess: (String) -> Unit,
    onIrAlRegistro: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var useLocalServer by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icono / Logo de AlimentaRed
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Eco,
                    contentDescription = "Logo AlimentaRed",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(46.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Título y Subtítulo según Mockup
            Text(
                text = "AlimentaRed",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 32.sp
            )

            Text(
                text = "Rescate y Distribución de Alimentos",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjeta Principal del Formulario
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Iniciar Sesión",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo de Correo Electrónico con icono
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electrónico") },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Campo de Contraseña con icono
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Estado de Carga / Éxito / Error
                    when (val state = uiState) {
                        is AuthUiState.Loading -> {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = if (useLocalServer) "Conectando al servidor local..." else "Conectando con el servidor en Render...",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        is AuthUiState.Success -> {
                            LaunchedEffect(Unit) {
                                onLoginSuccess(state.data.token)
                            }
                        }
                        is AuthUiState.Error -> {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = state.message,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        else -> {}
                    }

                    // Botón Principal de Ingreso
                    Button(
                        onClick = { viewModel.login(email.trim(), password) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = uiState !is AuthUiState.Loading && email.isNotBlank() && password.isNotBlank()
                    ) {
                        Text("Ingresar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Botón de Acceso Directo a Mockups Figma (Ideal para Pruebas y Calificación)
                    OutlinedButton(
                        onClick = { onLoginSuccess("demo_token_alimentared_2026") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Explorar Mockups (Modo Demo)", fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Enlace hacia Registro de Organización
                    TextButton(onClick = onIrAlRegistro) {
                        Text("¿No tienes cuenta? Regístrate aquí", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Selector Rápido de Servidor (Render vs Localhost)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (useLocalServer) Icons.Default.Computer else Icons.Default.Cloud,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = if (useLocalServer) "Servidor Local (Emulador)" else "Servidor Nube (Render)",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = if (useLocalServer) "10.0.2.2:8080" else "alimentared.onrender.com",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }

                    Switch(
                        checked = useLocalServer,
                        onCheckedChange = { checked ->
                            useLocalServer = checked
                            RetrofitClient.useLocalBackend(checked)
                        }
                    )
                }
            }
        }
    }
}
