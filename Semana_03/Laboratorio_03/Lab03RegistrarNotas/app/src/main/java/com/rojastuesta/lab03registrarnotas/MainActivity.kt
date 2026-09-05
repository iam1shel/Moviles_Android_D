package com.rojastuesta.lab03registrarnotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rojastuesta.lab03registrarnotas.ui.theme.Lab03RegistrarNotasTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab03RegistrarNotasTheme {
                PantallaRegistrarNotas()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistrarNotas() {
    var notaFundamentos by remember { mutableIntStateOf(0) }
    var notaPoo by remember { mutableIntStateOf(0) }
    var notaMoviles by remember { mutableIntStateOf(0) }
    var notaBd by remember { mutableIntStateOf(0) }
    var redondear by remember { mutableStateOf(false) }
    var confirmado by remember { mutableStateOf(false) }
    var mostrarResultado by remember { mutableStateOf(false) }

    val ponderado =
        notaFundamentos * 0.20 +
                notaPoo * 0.25 +
                notaMoviles * 0.30 +
                notaBd * 0.25
    val promedioFinal = if (redondear) ponderado.roundToInt().toDouble() else ponderado
    val observacion = observacionDe(promedioFinal)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Notas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.16f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            FilaCurso(
                nombre = "Fundamentos de Programación",
                peso = "20%",
                nota = notaFundamentos,
                onNotaChange = { notaFundamentos = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            FilaCurso(
                nombre = "Programación Orientada a Objetos",
                peso = "25%",
                nota = notaPoo,
                onNotaChange = { notaPoo = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            FilaCurso(
                nombre = "Programación en Móviles",
                peso = "30%",
                nota = notaMoviles,
                onNotaChange = { notaMoviles = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            FilaCurso(
                nombre = "Base de Datos",
                peso = "25%",
                nota = notaBd,
                onNotaChange = { notaBd = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Redondear promedio final",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = redondear,
                    onCheckedChange = { redondear = it }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = confirmado,
                    onCheckedChange = { confirmado = it }
                )
                Text("Confirmo que las notas son correctas")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { mostrarResultado = true },
                enabled = confirmado,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("CALCULAR PROMEDIO")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    notaFundamentos = 0
                    notaPoo = 0
                    notaMoviles = 0
                    notaBd = 0
                    redondear = false
                    confirmado = false
                    mostrarResultado = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("LIMPIAR")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!mostrarResultado) {
                Text(
                    text = "Asigna las notas y confirma para calcular",
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Resultado",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Promedio ponderado: " + String.format("%.2f", ponderado))
                        Text(
                            text = if (redondear) {
                                "Promedio final: ${ponderado.roundToInt()} (redondeado)"
                            } else {
                                "Promedio final: " + String.format("%.2f", promedioFinal)
                            },
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Aporte por curso:", style = MaterialTheme.typography.titleSmall)
                        Text(
                            "Fundamentos: $notaFundamentos × 20% = " +
                                    String.format("%.2f", notaFundamentos * 0.20)
                        )
                        Text(
                            "POO: $notaPoo × 25% = " +
                                    String.format("%.2f", notaPoo * 0.25)
                        )
                        Text(
                            "Móviles: $notaMoviles × 30% = " +
                                    String.format("%.2f", notaMoviles * 0.30)
                        )
                        Text(
                            "Base de Datos: $notaBd × 25% = " +
                                    String.format("%.2f", notaBd * 0.25)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            color = observacion.color,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = observacion.texto,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "✓ Promedio calculado correctamente",
                    color = Color(0xFF2E7D32)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Desarrollado por: Rojas Tuesta Luz Mishel",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

data class Observacion(val texto: String, val color: Color)

fun observacionDe(promedioFinal: Double): Observacion {
    return when {
        promedioFinal >= 17 -> Observacion("EXCELENTE", Color(0xFF1B5E20))
        promedioFinal >= 13 -> Observacion("APROBADO", Color(0xFF2E7D32))
        promedioFinal >= 10 -> Observacion("EN RECUPERACIÓN", Color(0xFFF9A825))
        else -> Observacion("DESAPROBADO", Color(0xFFC62828))
    }
}

@Composable
fun FilaCurso(
    nombre: String,
    peso: String,
    nota: Int,
    onNotaChange: (Int) -> Unit
) {
    val colorBadge = if (nota < 13) Color(0xFFC62828) else Color(0xFF2E7D32)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$nombre ($peso)",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    color = colorBadge,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "$nota",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
            Slider(
                value = nota.toFloat(),
                onValueChange = { onNotaChange(it.toInt()) },
                valueRange = 0f..20f,
                steps = 19
            )
        }
    }
}