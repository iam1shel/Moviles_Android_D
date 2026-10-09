package com.saludplus.citas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.ui.theme.AmarilloEstrella
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulGris
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.AzulSuave
import com.saludplus.citas.ui.theme.AzulTexto
import com.saludplus.citas.ui.theme.FondoDisponible
import com.saludplus.citas.ui.theme.GrisBorde
import com.saludplus.citas.ui.theme.TextoDisponible

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaludPlusTopBar(
    title: String,
    onVolver: (() -> Unit)? = null,
    actions: @Composable () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = AzulTexto
            )
        },
        navigationIcon = {
            if (onVolver != null) {
                IconButton(onClick = onVolver) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = AzulTexto
                    )
                }
            }
        },
        actions = { actions() },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
fun SaludPlusPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AzulPrimario,
            contentColor = Color.White,
            disabledContainerColor = AzulPrimario.copy(alpha = 0.4f)
        )
    ) {
        Text(text = text, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun SaludPlusTextLink(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(onClick = onClick, modifier = modifier) {
        Text(
            text = text,
            color = AzulPrimario,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun CampoConIcono(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .border(1.dp, GrisBorde, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = AzulPrimario)
        }
        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, GrisBorde, RoundedCornerShape(14.dp))
                .background(Color.White)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = AzulPrimario.copy(alpha = 0.75f)
            )
            Spacer(Modifier.height(2.dp))
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                visualTransformation = visualTransformation,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = AzulTexto,
                    fontWeight = FontWeight.Medium
                ),
                cursorBrush = SolidColor(AzulPrimario),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun AvatarMedico(
    nombre: String,
    modifier: Modifier = Modifier,
    size: Int = 56
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(AzulClaro),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = nombre,
            tint = AzulPrimario,
            modifier = Modifier.size((size * 0.55f).dp)
        )
    }
}

@Composable
fun TarjetaMedicoResumen(
    medico: Medico,
    especialidad: String,
    modifier: Modifier = Modifier,
    cmp: String? = medico.cmp
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AzulSuave)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarMedico(nombre = medico.nombre, size = 64)
        Spacer(Modifier.width(12.dp))
        Column {
            Text(medico.nombre, fontWeight = FontWeight.Bold, color = AzulTexto)
            Text(especialidad, color = AzulPrimario.copy(alpha = 0.85f))
            if (cmp != null) {
                Text("CMP: $cmp", color = AzulTexto.copy(alpha = 0.8f))
            }
        }
    }
}

@Composable
fun FilaMedicoLista(
    medico: Medico,
    especialidad: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, GrisBorde, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarMedico(nombre = medico.nombre)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(medico.nombre, fontWeight = FontWeight.Bold, color = AzulTexto)
            Text(especialidad, color = AzulGris, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = AmarilloEstrella,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "${medico.rating} (${medico.reseñas})",
                    style = MaterialTheme.typography.bodySmall,
                    color = AzulTexto
                )
            }
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .clip(RoundedCornerShape(50))
                    .background(FondoDisponible)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = medico.disponibilidad,
                    color = TextoDisponible,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun IconoCircularPastel(
    icon: ImageVector,
    tint: Color,
    fondo: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(fondo),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = tint)
    }
}

@Composable
fun FilaDetalleCita(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(AzulSuave),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = AzulPrimario)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, fontWeight = FontWeight.Bold, color = AzulTexto)
            Text(value, color = AzulTexto.copy(alpha = 0.85f))
        }
    }
}
