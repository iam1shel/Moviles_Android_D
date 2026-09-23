package com.rojastuesta.tecsupfit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rojastuesta.tecsupfit.model.Reservation
import com.rojastuesta.tecsupfit.model.ReservationStatus
import com.rojastuesta.tecsupfit.ui.theme.GrayCard
import com.rojastuesta.tecsupfit.ui.theme.GrayTextSecondary
import com.rojastuesta.tecsupfit.ui.theme.StatusCompletedBg
import com.rojastuesta.tecsupfit.ui.theme.StatusCompletedText
import com.rojastuesta.tecsupfit.ui.theme.StatusConfirmedBg
import com.rojastuesta.tecsupfit.ui.theme.StatusConfirmedText

@Composable
fun ReservationCard(
    reservation: Reservation,
    modifier: Modifier = Modifier
) {
    val isConfirmed = reservation.status == ReservationStatus.CONFIRMADA
    val badgeBg = if (isConfirmed) StatusConfirmedBg else StatusCompletedBg
    val badgeTextColor = if (isConfirmed) StatusConfirmedText else StatusCompletedText
    val statusText = if (isConfirmed) "Confirmada" else "Completada"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = GrayCard),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            // Vertical Green Bar on Left Side for Confirmed Reservations
            if (isConfirmed) {
                Box(
                    modifier = Modifier
                        .width(6.dp)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.primary)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = reservation.className,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = reservation.timeInfo,
                        fontSize = 13.sp,
                        color = GrayTextSecondary
                    )
                }

                // Status Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(badgeBg)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = statusText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeTextColor
                    )
                }
            }
        }
    }
}
