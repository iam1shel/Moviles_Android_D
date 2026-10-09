package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.R
import com.saludplus.citas.ui.components.SaludPlusPrimaryButton
import com.saludplus.citas.ui.components.SaludPlusTextLink
import com.saludplus.citas.ui.theme.AzulGris
import com.saludplus.citas.ui.theme.AzulTexto

@Composable
fun SplashScreen(
    onRegistrarse: () -> Unit,
    onIniciarSesion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.ic_saludplus_logo),
                contentDescription = "Logo SaludPlus",
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Clínica",
                color = AzulTexto,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "SaludPlus",
                color = AzulTexto,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Tu salud, nuestra prioridad",
                color = AzulGris,
                fontSize = 15.sp
            )
        }

        Image(
            painter = painterResource(R.drawable.img_doctor_hero),
            contentDescription = "Doctor SaludPlus",
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            contentScale = ContentScale.Fit
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            SaludPlusPrimaryButton(
                text = "Comenzar",
                onClick = onRegistrarse
            )
            Spacer(modifier = Modifier.height(4.dp))
            SaludPlusTextLink(
                text = "Ya tengo una cuenta",
                onClick = onIniciarSesion
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Demo: juan@correo.com / 123456",
                color = AzulGris,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
