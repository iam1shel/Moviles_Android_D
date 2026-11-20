package com.tecsup.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.campusgps.data.LugaresRepositoryLocal
import com.tecsup.campusgps.data.PreferenciasRepositoryDataStore
import com.tecsup.campusgps.data.UbicacionRepositoryGoogle
import com.tecsup.campusgps.ui.PantallaMapa
import com.tecsup.campusgps.viewmodel.CampusViewModelFactory
import com.tecsup.campusgps.viewmodel.MapaViewModel

private val CampusColorScheme = lightColorScheme(
    primary = Color(0xFF1565A8),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEAF4FC),
    onPrimaryContainer = Color(0xFF0D3B66),
    secondary = Color(0xFF2E9D68),
    onSecondary = Color.White,
    error = Color(0xFFC62828),
    errorContainer = Color(0xFFFDE8E8),
    onErrorContainer = Color(0xFF8A1C1C),
    surfaceVariant = Color(0xFFF8F9FA),
    onSurfaceVariant = Color(0xFF667085)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = CampusViewModelFactory(
            UbicacionRepositoryGoogle(applicationContext),
            LugaresRepositoryLocal(),
            PreferenciasRepositoryDataStore(applicationContext)
        )
        setContent {
            MaterialTheme(colorScheme = CampusColorScheme) {
                val vm: MapaViewModel = viewModel(factory = factory)
                PantallaMapa(vm)
            }
        }
    }
}
