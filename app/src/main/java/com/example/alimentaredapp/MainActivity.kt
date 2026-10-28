package com.example.alimentaredapp

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.alimentaredapp.ui.screens.AppNavigation
import com.example.alimentaredapp.ui.theme.AlimentaRedAppTheme
import com.example.alimentaredapp.util.LocationHelper

class MainActivity : ComponentActivity() {

    // Solicitar permiso de ubicación automáticamente al abrir la app
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        if (fineGranted || coarseGranted) {
            LocationHelper.obtenerUbicacionLegible(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Solicitamos permiso al abrir la aplicación
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        // Si ya tenía permisos previos, obtenemos la ubicación inmediatamente
        if (LocationHelper.tienePermisos(this)) {
            LocationHelper.obtenerUbicacionLegible(this)
        }

        setContent {
            // Forzamos fondo blanco y tema claro como en los mockups de Figma
            AlimentaRedAppTheme(darkTheme = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}
