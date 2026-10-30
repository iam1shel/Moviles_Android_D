package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.SelectorRol
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.ui.theme.AzulVet
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

@Composable
fun LoginScreen(
    vm: SesionViewModel,
    modoOscuro: Boolean,
    correoInicial: String? = null,
    claveInicial: String? = null,
    onRegistrar: () -> Unit,
    onIngreso: (Rol) -> Unit
) {
    var rol by remember { mutableStateOf(Rol.CLIENTE) }
    var correo by remember { mutableStateOf(correoInicial ?: "ana@correo.test") }
    var clave by remember { mutableStateOf(claveInicial ?: "1234") }
    var conservarDatos by remember { mutableStateOf(!correoInicial.isNullOrBlank()) }
    val error by vm.error.collectAsState()
    val snack = remember { SnackbarHostState() }

    LaunchedEffect(correoInicial, claveInicial) {
        if (!correoInicial.isNullOrBlank()) {
            rol = Rol.CLIENTE
            correo = correoInicial
            clave = claveInicial.orEmpty()
            conservarDatos = true
            snack.showSnackbar("Cuenta creada. Ingresa con estos datos.")
        }
    }
    LaunchedEffect(rol) {
        if (conservarDatos && rol == Rol.CLIENTE) {
            conservarDatos = false
            return@LaunchedEffect
        }
        when (rol) {
            Rol.CLIENTE -> {
                correo = "ana@correo.test"
                clave = "1234"
            }
            Rol.ADMIN -> {
                correo = "vet@huellitas.test"
                clave = "admin"
            }
            Rol.ADMINISTRADOR -> {
                correo = "admin@huellitas.test"
                clave = "admin123"
            }
        }
    }
    LaunchedEffect(error) {
        error?.let {
            snack.showSnackbar(it)
            vm.limpiarError()
        }
    }

    VetTheme(rol = rol, darkTheme = modoOscuro) {
        Scaffold(
            snackbarHost = { SnackbarHost(snack) },
            containerColor = if (modoOscuro) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surface
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(28.dp))
                Text(
                    "Bienvenido",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    letterSpacing = (-0.4).sp
                )
                Text(
                    "Ingresa para continuar",
                    color = Color(0xFF94A3B8),
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(22.dp))
                SelectorRol(rol = rol, onRol = { rol = it })
                Spacer(Modifier.height(20.dp))
                VetTextField(correo, { correo = it }, "Correo", icono = Icons.Outlined.Email)
                Spacer(Modifier.height(8.dp))
                VetTextField(
                    clave,
                    { clave = it },
                    "Contraseña",
                    icono = Icons.Outlined.Lock,
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    "¿Olvidaste tu contraseña?",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable { }
                )
                Spacer(Modifier.height(28.dp))
                VetButton("Ingresar", onClick = { vm.login(correo, clave, rol, onIngreso) })
                if (rol == Rol.CLIENTE) {
                    Spacer(Modifier.height(14.dp))
                    Text(
                        "¿No tienes cuenta? Regístrate",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable(onClick = onRegistrar)
                    )
                } else if (rol == Rol.ADMIN) {
                    Spacer(Modifier.height(22.dp))
                    Surface(
                        color = if (modoOscuro) MaterialTheme.colorScheme.primaryContainer else Color(0xFFEFF6FF),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                            Text(
                                "Modo práctica",
                                fontWeight = FontWeight.SemiBold,
                                color = if (modoOscuro) MaterialTheme.colorScheme.onPrimaryContainer else AzulVet,
                                fontSize = 14.sp
                            )
                            Text(
                                "Puedes cambiar de perfil desde Ajustes para probar el dueño y el veterinario.",
                                fontSize = 13.sp,
                                color = if (modoOscuro) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )
                        }
                    }
                } else {
                    Spacer(Modifier.height(22.dp))
                    Surface(
                        color = if (modoOscuro) MaterialTheme.colorScheme.primaryContainer else Color(0xFFFEF3C7),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                            Text(
                                "Cuenta de administrador",
                                fontWeight = FontWeight.SemiBold,
                                color = if (modoOscuro) MaterialTheme.colorScheme.onPrimaryContainer else Color(0xFF92400E),
                                fontSize = 14.sp
                            )
                            Text(
                                "Código ADM-001 · Camila Torres\nCorreo admin@huellitas.test · contraseña admin123",
                                fontSize = 13.sp,
                                color = if (modoOscuro) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
