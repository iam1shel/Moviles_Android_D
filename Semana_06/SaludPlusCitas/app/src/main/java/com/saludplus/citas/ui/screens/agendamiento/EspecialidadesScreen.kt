package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.IconoCircularPastel
import com.saludplus.citas.ui.components.SaludPlusTopBar
import com.saludplus.citas.ui.theme.AzulGris
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.AzulTexto
import com.saludplus.citas.ui.theme.GrisBorde
import com.saludplus.citas.ui.theme.GrisFondo

@Composable
fun EspecialidadesScreen(
    sede: String? = null,
    onVolver: () -> Unit,
    onSeleccionar: (Int) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val lista = Repositorio.buscarEspecialidades(query)
    val sedeSeleccionada = sede?.takeIf { it.isNotBlank() }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            SaludPlusTopBar(title = "Especialidades", onVolver = onVolver)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            if (sedeSeleccionada != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = AzulPrimario)
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text("Sede: $sedeSeleccionada", color = AzulPrimario, fontWeight = FontWeight.SemiBold)
                }
            }
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar especialidad...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AzulGris) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = GrisFondo,
                    unfocusedContainerColor = GrisFondo,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(lista, key = { it.id }) { esp ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSeleccionar(esp.id) }
                            .padding(vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconoCircularPastel(
                            icon = iconoDe(esp.nombre),
                            tint = Color(esp.colorIcono),
                            fondo = Color(esp.colorFondo)
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 14.dp)
                        ) {
                            Text(esp.nombre, fontWeight = FontWeight.Bold, color = AzulTexto)
                            Text(esp.descripcion, color = AzulGris)
                        }
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = AzulTexto
                        )
                    }
                    HorizontalDivider(color = GrisBorde)
                }
            }
        }
    }
}

private fun iconoDe(nombre: String): ImageVector = when {
    nombre.contains("Pedi", ignoreCase = true) -> Icons.Default.ChildCare
    nombre.contains("Gine", ignoreCase = true) -> Icons.Default.Favorite
    nombre.contains("Cardio", ignoreCase = true) -> Icons.Default.Favorite
    nombre.contains("Oftal", ignoreCase = true) -> Icons.Default.Visibility
    nombre.contains("Trauma", ignoreCase = true) -> Icons.Default.LocalHospital
    nombre.contains("Derma", ignoreCase = true) -> Icons.Default.Face
    else -> Icons.Default.Person
}
