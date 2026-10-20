package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.Sexo
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.HistorialItem
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.graficos.GraficoLineas
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel
import java.time.LocalDate

@Composable
fun FichaMascotaScreen(
    vm: MascotaViewModel,
    onBack: () -> Unit,
    onEditar: (Int) -> Unit,
    onPedirCita: (Int) -> Unit,
    esVeterinario: Boolean = false
) {
    val mascota by vm.mascota.collectAsState()
    val historial by vm.historial.collectAsState()
    val pesos by vm.pesoHistorial.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    var tab by remember { mutableIntStateOf(1) }
    var confirmarBorrar by remember { mutableStateOf(false) }
    val snack = remember { SnackbarHostState() }
    val m = mascota
    LaunchedEffect(mensaje) { mensaje?.let { snack.showSnackbar(it); vm.limpiarMensaje() } }

    Scaffold(
        topBar = {
            AppTopBar(m?.nombre ?: "Mascota", onBack) {
                if (m != null && !esVeterinario) {
                    IconButton(onClick = { confirmarBorrar = true }) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Eliminar")
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(snack) }
    ) { padding ->
        if (m == null) {
            Text("Cargando...", modifier = Modifier.padding(padding).padding(16.dp))
            return@Scaffold
        }
        val edad = (LocalDate.now().year - m.anioNacimiento).coerceAtLeast(0)
        val especieTxt = when (m.especie) {
            Especie.PERRO.name -> "Perro"
            Especie.GATO.name -> "Gato"
            else -> "Otro"
        }
        val sexoTxt = if (m.sexo == Sexo.HEMBRA.name) "Hembra" else "Macho"
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                MascotaAvatar(m.especie, tamano = 76.dp, nombre = m.nombre)
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(m.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(
                        if (m.especie == Especie.GATO.name) {
                            val gato = if (m.sexo == Sexo.HEMBRA.name) "Gata" else "Gato"
                            "$gato ${m.raza.lowercase()} · $edad años"
                        } else {
                            "$especieTxt ${m.raza.lowercase()} · $edad años"
                        },
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text("$sexoTxt · ${m.peso} kg", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(6.dp))
                    Surface(
                        color = Color(0xFFD1FAE5),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            "Vacunas al día",
                            color = Color(0xFF059669),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            if (!esVeterinario) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { onPedirCita(m.id) },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Icon(Icons.Outlined.Pets, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Pedir cita")
                    }
                    OutlinedButton(
                        onClick = { onEditar(m.id) },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Icon(Icons.Outlined.Edit, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Editar")
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            TabRow(selectedTabIndex = tab) {
                Tab(selected = tab == 0, onClick = { tab = 0 }, text = { Text("Datos") })
                Tab(selected = tab == 1, onClick = { tab = 1 }, text = { Text("Historial clínico") })
            }
            Spacer(Modifier.height(12.dp))
            if (tab == 0) {
                Text("Raza: ${m.raza}")
                Text("Año de nacimiento: ${m.anioNacimiento}")
                Text("Peso actual: ${m.peso} kg")
                Spacer(Modifier.height(12.dp))
                Text("Evolución del peso", fontWeight = FontWeight.SemiBold)
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    GraficoLineas(pesos)
                }
            } else {
                if (historial.isEmpty()) {
                    Text("Aún no tiene atenciones", color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        historial.forEach { HistorialItem(it) }
                    }
                }
            }
        }
    }
    if (confirmarBorrar && m != null) {
        AlertDialog(
            onDismissRequest = { confirmarBorrar = false },
            title = { Text("Eliminar mascota") },
            text = { Text("¿Eliminar a ${m.nombre}?") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarBorrar = false
                    vm.eliminar(m, onBack)
                }) { Text("Eliminar") }
            },
            dismissButton = { TextButton(onClick = { confirmarBorrar = false }) { Text("Cancelar") } }
        )
    }
}
