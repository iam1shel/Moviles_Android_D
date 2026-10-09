package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.CampoConIcono
import com.saludplus.citas.ui.components.SaludPlusPrimaryButton
import com.saludplus.citas.ui.theme.AzulGris
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.AzulTexto

@Composable
fun RegistroScreen(
    onVolver: () -> Unit,
    onRegistrado: () -> Unit,
    onTerminos: () -> Unit,
    onIrLogin: () -> Unit = onVolver
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var mostrarExito by remember { mutableStateOf(false) }

    if (mostrarExito) {
        AlertDialog(
            onDismissRequest = { /* debe confirmar con el botón */ },
            title = {
                Text("Registro exitoso", fontWeight = FontWeight.Bold, color = AzulTexto)
            },
            text = {
                Text(
                    text = "Tu cuenta se creó correctamente. Ahora inicia sesión para continuar.",
                    color = AzulGris
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarExito = false
                        onRegistrado()
                    }
                ) {
                    Text("Iniciar sesión", color = AzulPrimario, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Crear cuenta",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = AzulTexto
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Regístrate para agendar tus citas",
            color = AzulGris,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(28.dp))

        CampoConIcono(
            value = nombre,
            onValueChange = { nombre = it; error = null },
            label = "Nombre completo",
            icon = Icons.Default.Person
        )
        Spacer(modifier = Modifier.height(14.dp))
        CampoConIcono(
            value = telefono,
            onValueChange = { telefono = it; error = null },
            label = "Teléfono",
            icon = Icons.Default.Phone
        )
        Spacer(modifier = Modifier.height(14.dp))
        CampoConIcono(
            value = correo,
            onValueChange = { correo = it; error = null },
            label = "Correo (opcional)",
            icon = Icons.Default.Email
        )
        Spacer(modifier = Modifier.height(14.dp))
        CampoConIcono(
            value = clave,
            onValueChange = { clave = it; error = null },
            label = "Contraseña",
            icon = Icons.Default.Lock,
            visualTransformation = PasswordVisualTransformation()
        )

        error?.let {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(24.dp))
        SaludPlusPrimaryButton(
            text = "Registrarme",
            onClick = {
                when {
                    nombre.isBlank() || telefono.isBlank() || clave.isBlank() ->
                        error = "Completa nombre, teléfono y contraseña"
                    clave.length < 4 -> error = "La contraseña debe tener al menos 4 caracteres"
                    !Repositorio.registrarUsuario(
                        nombre = nombre,
                        correo = correo.ifBlank { "$telefono@saludplus.local" },
                        telefono = telefono,
                        clave = clave
                    ) -> error = "Ese correo ya está registrado"
                    else -> {
                        error = null
                        mostrarExito = true
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = buildAnnotatedString {
                append("Al registrarte aceptas nuestros\n")
                withStyle(SpanStyle(color = AzulPrimario, fontWeight = FontWeight.Bold)) {
                    append("Términos y Condiciones")
                }
            },
            textAlign = TextAlign.Center,
            color = AzulGris,
            modifier = Modifier.clickable(onClick = onTerminos)
        )
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = buildAnnotatedString {
                append("¿Ya tienes cuenta? ")
                withStyle(SpanStyle(color = AzulPrimario, fontWeight = FontWeight.Bold)) {
                    append("Iniciar sesión")
                }
            },
            modifier = Modifier.clickable(onClick = onIrLogin),
            color = AzulTexto
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}
