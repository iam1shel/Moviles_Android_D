package com.tecsup.centrotareas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.centrotareas.model.CargaUi
import com.tecsup.centrotareas.model.PrimosUi
import com.tecsup.centrotareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {

    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    val progreso by vm.progreso.collectAsStateWithLifecycle()
    val corriendo by vm.corriendo.collectAsStateWithLifecycle()

    var toques by remember {
        mutableIntStateOf(0)
    }

    val primos by vm.primos.collectAsStateWithLifecycle()
    val carga by vm.carga.collectAsStateWithLifecycle()
    val cargando = carga is CargaUi.Cargando
    val simularError by vm.simularError.collectAsStateWithLifecycle()

    val tiempoUnaPorUna by vm.tiempoUnaPorUna.collectAsStateWithLifecycle()
    val tiempoALaVez by vm.tiempoALaVez.collectAsStateWithLifecycle()

    var numero by remember {
        mutableStateOf("500000")
    }

    val calculando = primos is PrimosUi.Calculando

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("CentroTareas")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // TARJETA 1: TAREA CON PROGRESO
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        text = "Tarea con progreso",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    Button(
                        onClick = {
                            toques++
                        }
                    ) {
                        Text("Tócame: $toques")
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { vm.tareaLarga() },
                            enabled = !corriendo
                        ) {
                            Text("Iniciar")
                        }

                        OutlinedButton(
                            onClick = { vm.cancelar() },
                            enabled = corriendo
                        ) {
                            Text("Cancelar")
                        }
                    }

                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("${(progreso * 100).toInt()} %")

                    Text(mensaje)
                }
            }

            // TARJETA 2: CONTAR NÚMEROS PRIMOS
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        text = "Contar números primos",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = numero,
                        onValueChange = { numero = it },
                        label = { Text("Hasta el número") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true,
                        enabled = !calculando,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Button(
                            onClick = {
                                val hasta = numero.toIntOrNull()
                                if (hasta != null && hasta >= 2) {
                                    vm.contar(hasta)
                                }
                            },
                            enabled = !calculando
                        ) {
                            Text("Contar")
                        }

                        OutlinedButton(
                            onClick = { vm.cancelarPrimos() },
                            enabled = calculando
                        ) {
                            Text("Cancelar")
                        }
                    }

                    when (val estado = primos) {

                        is PrimosUi.Inicial -> {
                            // Todavía no hay resultados
                        }

                        is PrimosUi.Calculando -> {
                            CircularProgressIndicator()
                            Text("Calculando...")
                        }

                        is PrimosUi.Listo -> {

                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFE0F2E1)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    Text(
                                        text = "Primos hasta $numero: ${estado.cantidad}",
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = "Tiempo: ${estado.milisegundos} ms"
                                    )
                                }
                            }
                        }

                        is PrimosUi.Cancelado -> {
                            Text("Cancelado")
                        }
                    }
                }
            }

            // TARJETA 3: DOS CARGAS
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        text = "Dos cargas",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Switch(
                            checked = simularError,
                            onCheckedChange = { valor ->
                                vm.cambiarSimularError(valor)
                            },
                            enabled = !cargando
                        )

                        Text("Simular error")
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        OutlinedButton(
                            onClick = {
                                vm.cargarUnaPorUna()
                            },
                            enabled = !cargando
                        ) {
                            Text("Una por una")
                        }

                        Button(
                            onClick = {
                                vm.cargarALaVez()
                            },
                            enabled = !cargando
                        ) {
                            Text("A la vez")
                        }
                    }

                    when (val estado = carga) {

                        is CargaUi.Inicial -> {
                            // Todavía no se han cargado datos
                        }

                        is CargaUi.Cargando -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CircularProgressIndicator()
                                Text(estado.mensaje)
                            }
                        }

                        is CargaUi.Exito -> {

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFE0F2E1)
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {

                                    Text(
                                        text = "Modo: ${estado.modo}",
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text("Perfil: ${estado.perfil}")

                                    Text("Pedidos: ${estado.pedidos}")

                                    Text("Tiempo: ${estado.milisegundos} ms")
                                }
                            }
                        }

                        is CargaUi.Error -> {

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer
                                    )
                                ) {
                                    Text(
                                        text = estado.mensaje,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }

                                OutlinedButton(
                                    onClick = {
                                        vm.cargarALaVez()
                                    }
                                ) {
                                    Text("Reintentar")
                                }
                            }
                        }
                    }
                }
            }

            // SECCIÓN ADICIONAL: COMPARACIÓN DE RENDIMIENTO (MEJORA 2)
            if (tiempoUnaPorUna != null && tiempoALaVez != null) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Comparación de rendimiento",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )

                        val tSeq = tiempoUnaPorUna ?: 0L
                        val tPar = tiempoALaVez ?: 0L
                        val ahorro = if (tSeq > tPar) tSeq - tPar else 0L
                        val mejora = if (tSeq > 0) (ahorro.toFloat() / tSeq * 100).toInt() else 0

                        Text("Una por una: $tSeq ms")
                        LinearProgressIndicator(
                            progress = { if (tSeq + tPar > 0) tSeq.toFloat() / (tSeq + tPar) else 0f },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text("A la vez: $tPar ms")
                        LinearProgressIndicator(
                            progress = { if (tSeq + tPar > 0) tPar.toFloat() / (tSeq + tPar) else 0f },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tiempo ahorrado: $ahorro ms (Mejora: $mejora %)",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}
