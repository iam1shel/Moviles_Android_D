package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
fun LoginScreen(
    onVolver: () -> Unit,
    onLoginOk: () -> Unit,
    onIrRegistro: () -> Unit = onVolver
) {
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(36.dp))
        Text(
            text = "Iniciar sesión",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = AzulTexto
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Ingresa para continuar",
            color = AzulGris,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Demo: juan@correo.com / 123456",
            color = AzulGris,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(28.dp))

        CampoConIcono(
            value = correo,
            onValueChange = { correo = it; error = null },
            label = "Correo",
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
            text = "Entrar",
            onClick = {
                when {
                    correo.isBlank() || clave.isBlank() ->
                        error = "Completa correo y contraseña"
                    !Repositorio.iniciarSesion(correo.trim(), clave) ->
                        error = "Correo o contraseña incorrectos"
                    else -> onLoginOk()
                }
            }
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = buildAnnotatedString {
                append("¿No tienes cuenta? ")
                withStyle(SpanStyle(color = AzulPrimario, fontWeight = FontWeight.Bold)) {
                    append("Crear cuenta")
                }
            },
            modifier = Modifier.clickable(onClick = onIrRegistro),
            color = AzulTexto
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}
