package com.example.alimentared.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

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
    onVolver: () -> Unit
) {
    // Datos de prueba simulando usuarios pendientes de aprobación
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
                text = "Solicitudes de registro pendientes de verificación documental",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            if (mensajeStatus != null) {
                Text(
                    text = mensajeStatus!!,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(usuariosPendientes) { usuario ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = usuario.nombreEntidad,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(text = "Rol solicitado: ${usuario.tipoRol}", style = MaterialTheme.typography.bodySmall)
                            Text(text = "NIT/RUT: ${usuario.nitORut}", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                            Text(text = "Contacto: ${usuario.correo}", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)

                            Spacer(modifier = Modifier.height(4.dp))

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
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Text("Aprobar", fontSize = 12.sp)
                                }

                                OutlinedButton(
                                    onClick = {
                                        mensajeStatus = "Solicitud de '${usuario.nombreEntidad}' RECHAZADA."
                                        usuariosPendientes = usuariosPendientes.filter { it.id != usuario.id }
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                                ) {
                                    Text("Rechazar", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}
