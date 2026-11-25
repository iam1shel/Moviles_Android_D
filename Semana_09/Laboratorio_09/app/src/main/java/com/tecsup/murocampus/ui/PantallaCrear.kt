package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.R
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCrear(
    viewModel: PostsViewModel,
    alVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var contenido by rememberSaveable { mutableStateOf("") }
    val crearState by viewModel.crearState.collectAsStateWithLifecycle()

    LaunchedEffect(crearState) {
        if (crearState is UiState.Exito) {
            titulo = ""
            contenido = ""
        }
    }

    val esValido = titulo.isNotBlank() && contenido.isNotBlank()
    val cargando = crearState is UiState.Cargando
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = alVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        modifier = modifier
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = titulo,
                onValueChange = {
                    titulo = it
                    if (crearState is UiState.Exito) {
                        viewModel.resetCrear()
                    }
                },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                enabled = !cargando
            )

            OutlinedTextField(
                value = contenido,
                onValueChange = {
                    contenido = it
                    if (crearState is UiState.Exito) {
                        viewModel.resetCrear()
                    }
                },
                label = { Text("Contenido") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(8.dp),
                enabled = !cargando
            )

            Button(
                onClick = {
                    if (esValido && !cargando) {
                        viewModel.crear(titulo, contenido)
                    }
                },
                modifier = Modifier
                    .width(120.dp)
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(50.dp),
                enabled = esValido && !cargando
            ) {
                if (cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(end = 4.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Text("Publicar", fontWeight = FontWeight.Bold, color = Color.White)
            }

            when (val state = crearState) {
                is UiState.Exito -> {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .dashedBorder(
                                color = Color(0xFF2E7D32),
                                strokeWidth = 2.dp,
                                cornerRadius = 8.dp,
                                dashLength = 8.dp,
                                gapLength = 4.dp
                            ),
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFE8F5E9),
                        contentColor = Color(0xFF1B5E20)
                    ) {
                        Text(
                            text = "Publicado con id ${state.datos.id}",
                            modifier = Modifier.padding(12.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                is UiState.Error -> {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ) {
                        Text(
                            text = state.mensaje,
                            modifier = Modifier.padding(12.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> {}
            }
        }
    }
}
