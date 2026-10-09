package com.saludplus.citas.ui.screens.sedes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.IconoCircularPastel
import com.saludplus.citas.ui.components.SaludPlusTopBar
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulGris
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.AzulTexto
import com.saludplus.citas.ui.theme.GrisFondo

@Composable
fun SedesScreen(
    onVolver: () -> Unit,
    onSeleccionarSede: (String) -> Unit
) {
    val sedes = Repositorio.listarSedes()

    Scaffold(
        containerColor = GrisFondo,
        topBar = {
            SaludPlusTopBar(title = "Sedes", onVolver = onVolver)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = "Elige una sede para agendar tu cita",
                    color = AzulGris,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            items(sedes, key = { it.id }) { sede ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSeleccionarSede(sede.nombre) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconoCircularPastel(
                            icon = Icons.Default.LocationOn,
                            tint = AzulPrimario,
                            fondo = AzulClaro
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(sede.nombre, fontWeight = FontWeight.Bold, color = AzulTexto)
                            Text("Toca para agendar en esta sede", color = AzulGris)
                        }
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = AzulTexto,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}
