package com.rojastuesta.tecsupfit.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rojastuesta.tecsupfit.model.GymClass
import com.rojastuesta.tecsupfit.ui.theme.GrayCard
import com.rojastuesta.tecsupfit.ui.theme.GrayTextSecondary
import com.rojastuesta.tecsupfit.ui.theme.GreenLightContainer

@Composable
fun ClassCard(
    gymClass: GymClass,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = getClassIcon(gymClass.iconType)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = GrayCard),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Simple Icon on the left
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(GreenLightContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = gymClass.name,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Class Name, Time and Room
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = gymClass.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${gymClass.time} • ${gymClass.room}",
                    fontSize = 13.sp,
                    color = GrayTextSecondary,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

fun getClassIcon(iconType: String): ImageVector {
    return when (iconType.uppercase()) {
        "YOGA" -> Icons.Default.SelfImprovement
        "CROSS" -> Icons.Default.FitnessCenter
        "SPINNING" -> Icons.Default.DirectionsBike
        else -> Icons.Default.SportsGymnastics
    }
}
