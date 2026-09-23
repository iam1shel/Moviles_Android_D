package com.example.semana05_navegacion.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.semana05_navegacion.ui.theme.PurpleLight
import com.example.semana05_navegacion.ui.theme.PurplePrimary
import com.example.semana05_navegacion.ui.theme.TextPrimary
import com.example.semana05_navegacion.ui.theme.TextSecondary

@Composable
fun InitialAvatar(
    iniciales: String,
    color: Color,
    size: Dp = 48.dp,
    textSize: Int = 16,
    hasBorder: Boolean = false
) {
    val modifier = if (hasBorder) {
        Modifier
            .size(size)
            .border(3.dp, Color.White, CircleShape)
            .clip(CircleShape)
            .background(color)
    } else {
        Modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = iniciales,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = textSize.sp
        )
    }
}

@Composable
fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(PurpleLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PurplePrimary
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(
                text = label,
                color = TextSecondary,
                fontSize = 12.sp
            )
            Text(
                text = value,
                color = TextPrimary,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }
    }
}
