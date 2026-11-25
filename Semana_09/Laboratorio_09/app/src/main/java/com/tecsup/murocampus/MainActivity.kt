package com.tecsup.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.murocampus.data.PostRepositoryRemote
import com.tecsup.murocampus.data.remote.RetrofitClient
import com.tecsup.murocampus.ui.PantallaCrear
import com.tecsup.murocampus.ui.PantallaDetalle
import com.tecsup.murocampus.ui.PantallaLista
import com.tecsup.murocampus.ui.theme.MuroCampusTheme
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.PostsViewModelFactory

sealed interface Pantalla {
    data object Lista : Pantalla
    data class Detalle(val id: Int) : Pantalla
    data object Crear : Pantalla
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT))

        setContent {
            MuroCampusTheme {
                val viewModel: PostsViewModel = viewModel(
                    factory = PostsViewModelFactory(PostsViewModel::class.java) {
                        PostsViewModel(PostRepositoryRemote(RetrofitClient.api))
                    }
                )

                var pantallaActual by remember { mutableStateOf<Pantalla>(Pantalla.Lista) }

                BackHandler(enabled = pantallaActual !is Pantalla.Lista) {
                    pantallaActual = Pantalla.Lista
                }

                when (pantallaActual) {
                    is Pantalla.Lista -> {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    title = { Text(stringResource(R.string.app_name)) },
                                    colors = TopAppBarDefaults.topAppBarColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                            }
                        ) { padding ->
                            PantallaLista(
                                viewModel = viewModel,
                                alSeleccionarPost = { id ->
                                    viewModel.abrirDetalle(id)
                                    pantallaActual = Pantalla.Detalle(id)
                                },
                                alIrACrear = {
                                    viewModel.resetCrear()
                                    pantallaActual = Pantalla.Crear
                                },
                                modifier = Modifier.padding(padding)
                            )
                        }
                    }
                    is Pantalla.Detalle -> {
                        PantallaDetalle(
                            viewModel = viewModel,
                            alVolver = { pantallaActual = Pantalla.Lista }
                        )
                    }
                    is Pantalla.Crear -> {
                        PantallaCrear(
                            viewModel = viewModel,
                            alVolver = { pantallaActual = Pantalla.Lista }
                        )
                    }
                }
            }
        }
    }
}
