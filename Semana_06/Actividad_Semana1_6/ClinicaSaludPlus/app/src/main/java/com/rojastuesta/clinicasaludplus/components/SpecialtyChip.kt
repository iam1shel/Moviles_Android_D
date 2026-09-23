package com.rojastuesta.clinicasaludplus.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rojastuesta.clinicasaludplus.ui.theme.PurpleLight
import com.rojastuesta.clinicasaludplus.ui.theme.PurplePrimary
import com.rojastuesta.clinicasaludplus.ui.theme.TextPrimary
import com.rojastuesta.clinicasaludplus.ui.theme.SurfaceWhite

@Composable
fun SpecialtyChip(
    specialtyName: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) PurplePrimary else PurpleLight
    val textColor = if (isSelected) SurfaceWhite else TextPrimary

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = modifier.clickable { onClick() }
    ) {
        Text(
            text = specialtyName,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )
    }
}
