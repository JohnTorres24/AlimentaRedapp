package com.example.alimentaredapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SolicitudUsuarioItem(
    val id: Long,
    val nombreEntidad: String,
    val tipoRol: String, // Donante u Organización Receptora
    val nitORut: String,
    val correo: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionUsuariosScreen(
    onVolver: (() -> Unit)? = null
) {
    var usuariosPendientes by remember {
        mutableStateOf(
            listOf(
                SolicitudUsuarioItem(1, "Fundación Comedor Esperanza", "Organización Receptora", "900.123.456-1", "contacto@esperanza.org"),
                SolicitudUsuarioItem(2, "Supermercado El Campo", "Donante", "800.987.654-2", "donaciones@elcampo.com"),
                SolicitudUsuarioItem(3, "Asociación Pan de Vida", "Organización Receptora", "901.555.777-3", "admin@pandevida.org")
            )
        )
    }

    var mensajeStatus by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Admin - Validación de Cuentas") },
                navigationIcon = {
                    if (onVolver != null) {
                        IconButton(onClick = onVolver) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Solicitudes de registro pendientes de verificación documental (RF02)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            if (mensajeStatus != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(
                        text = mensajeStatus!!,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(usuariosPendientes) { usuario ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = usuario.nombreEntidad,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )

                                Surface(
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    shape = RoundedCornerShape(20.dp)
                                ) {
                                    Text(
                                        text = usuario.tipoRol,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Badge, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.outline)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "NIT/RUT: ${usuario.nitORut}", fontSize = 13.sp, color = MaterialTheme.colorScheme.outline)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.outline)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Contacto: ${usuario.correo}", fontSize = 13.sp, color = MaterialTheme.colorScheme.outline)
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        mensajeStatus = "Cuenta de '${usuario.nombreEntidad}' APROBADA exitosamente."
                                        usuariosPendientes = usuariosPendientes.filter { it.id != usuario.id }
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Aprobar", fontSize = 12.sp)
                                }

                                OutlinedButton(
                                    onClick = {
                                        mensajeStatus = "Solicitud de '${usuario.nombreEntidad}' RECHAZADA."
                                        usuariosPendientes = usuariosPendientes.filter { it.id != usuario.id }
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Rechazar", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            if (onVolver != null) {
                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Volver")
                }
            }
        }
    }
}
