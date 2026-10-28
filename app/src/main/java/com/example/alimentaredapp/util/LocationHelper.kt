package com.example.alimentaredapp.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

object LocationHelper {
    private val _direccionDetectada = MutableStateFlow("Cajicá, Cundinamarca")
    val direccionDetectada: StateFlow<String> = _direccionDetectada

    fun setDireccion(nuevaDireccion: String) {
        _direccionDetectada.value = nuevaDireccion
    }

    fun tienePermisos(context: Context): Boolean {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    fun obtenerUbicacionLegible(context: Context) {
        if (!tienePermisos(context)) return

        try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return
            val providers = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER, LocationManager.PASSIVE_PROVIDER)
            var bestLocation: Location? = null

            for (provider in providers) {
                try {
                    if (locationManager.isProviderEnabled(provider)) {
                        val loc = locationManager.getLastKnownLocation(provider)
                        if (loc != null && (bestLocation == null || loc.accuracy < bestLocation.accuracy)) {
                            bestLocation = loc
                        }
                    }
                } catch (_: SecurityException) {}
            }

            if (bestLocation != null) {
                val geocoder = Geocoder(context, Locale("es", "CO"))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(bestLocation.latitude, bestLocation.longitude, 1) { addresses ->
                        if (addresses.isNotEmpty()) {
                            val addr = addresses[0]
                            val ciudad = addr.locality ?: addr.subAdminArea ?: ""
                            val departamento = addr.adminArea ?: ""
                            val resultado = when {
                                ciudad.isNotBlank() && departamento.isNotBlank() -> "$ciudad, $departamento"
                                addr.getAddressLine(0) != null -> addr.getAddressLine(0)
                                else -> "Cajicá, Cundinamarca"
                            }
                            _direccionDetectada.value = resultado
                        }
                    }
                } else {
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(bestLocation.latitude, bestLocation.longitude, 1)
                    if (!addresses.isNullOrEmpty()) {
                        val addr = addresses[0]
                        val ciudad = addr.locality ?: addr.subAdminArea ?: ""
                        val departamento = addr.adminArea ?: ""
                        val resultado = when {
                            ciudad.isNotBlank() && departamento.isNotBlank() -> "$ciudad, $departamento"
                            addr.getAddressLine(0) != null -> addr.getAddressLine(0)
                            else -> "Cajicá, Cundinamarca"
                        }
                        _direccionDetectada.value = resultado
                    }
                }
            } else {
                _direccionDetectada.value = "Cajicá, Cundinamarca"
            }
        } catch (_: Exception) {
            _direccionDetectada.value = "Cajicá, Cundinamarca"
        }
    }
}
