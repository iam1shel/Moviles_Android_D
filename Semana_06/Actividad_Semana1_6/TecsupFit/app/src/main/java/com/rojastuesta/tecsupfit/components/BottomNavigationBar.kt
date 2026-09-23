package com.rojastuesta.tecsupfit.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rojastuesta.tecsupfit.navigation.Screen
import com.rojastuesta.tecsupfit.ui.theme.GrayCardBorder
import com.rojastuesta.tecsupfit.ui.theme.GrayTextSecondary

data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem(
            title = "Inicio",
            route = Screen.Home.route,
            icon = Icons.Default.Home
        ),
        BottomNavItem(
            title = "Reservas",
            route = Screen.Reservations.route,
            icon = Icons.Default.CalendarToday
        ),
        BottomNavItem(
            title = "Rutinas",
            route = Screen.Routines.route,
            icon = Icons.Default.FitnessCenter
        ),
        BottomNavItem(
            title = "Perfil",
            route = Screen.Profile.route,
            icon = Icons.Default.Person
        )
    )

    Column {
        HorizontalDivider(
            thickness = 1.dp,
            color = GrayCardBorder
        )

        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 0.dp
        ) {
            items.forEach { item ->
                val isSelected = currentRoute == item.route

                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onNavigate(item.route) },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = GrayTextSecondary,
                        unselectedTextColor = GrayTextSecondary,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}
