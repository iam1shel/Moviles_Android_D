package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

@Composable
fun RegistroScreen(
    vm: SesionViewModel,
    modoOscuro: Boolean,
    onBack: () -> Unit,
    onCreada: (correo: String, clave: String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }
    val errorNombre by vm.errorNombre.collectAsState()
    val errorCorreo by vm.errorCorreo.collectAsState()
    val errorClave by vm.errorClave.collectAsState()
    val errorConfirmar by vm.errorConfirmar.collectAsState()

    VetTheme(darkTheme = modoOscuro) {
        Scaffold(topBar = { AppTopBar("Crear cuenta", onBack) }) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    "Registro de dueño",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                Text(
                    "Con esta cuenta registras mascotas y pides citas.",
                    color = Color(0xFF94A3B8),
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(20.dp))
                VetTextField(nombre, { nombre = it }, "Nombre", icono = Icons.Outlined.Person, error = errorNombre)
                Spacer(Modifier.height(8.dp))
                VetTextField(
                    correo,
                    { correo = it },
                    "Correo",
                    icono = Icons.Outlined.Email,
                    error = errorCorreo,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(Modifier.height(8.dp))
                VetTextField(
                    clave,
                    { clave = it },
                    "Contraseña",
                    icono = Icons.Outlined.Lock,
                    error = errorClave,
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(Modifier.height(8.dp))
                VetTextField(
                    confirmar,
                    { confirmar = it },
                    "Confirmar contraseña",
                    icono = Icons.Outlined.Lock,
                    error = errorConfirmar,
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(Modifier.height(28.dp))
                VetButton("Crear cuenta", onClick = {
                    vm.registrar(nombre, correo, clave, confirmar, onCreada)
                })
                Spacer(Modifier.height(8.dp))
                VetButton("Cancelar", onClick = onBack, outlined = true)
                Spacer(Modifier.height(16.dp))
                Text(
                    "Al terminar vuelves al ingreso con estos datos.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
