package com.rojastuesta.clinicasaludplus.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rojastuesta.clinicasaludplus.screens.AppointmentScreen
import com.rojastuesta.clinicasaludplus.screens.AppointmentsScreen
import com.rojastuesta.clinicasaludplus.screens.ConfirmationScreen
import com.rojastuesta.clinicasaludplus.screens.DoctorProfileScreen
import com.rojastuesta.clinicasaludplus.screens.HomeScreen
import com.rojastuesta.clinicasaludplus.screens.MedicalHistoryScreen
import com.rojastuesta.clinicasaludplus.screens.ProfileScreen
import com.rojastuesta.clinicasaludplus.ui.theme.PurpleLight
import com.rojastuesta.clinicasaludplus.ui.theme.PurplePrimary
import com.rojastuesta.clinicasaludplus.ui.theme.TextPrimary
import com.rojastuesta.clinicasaludplus.ui.theme.TextSecondary
import kotlinx.coroutines.launch

data class DrawerMenuItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val drawerItems = listOf(
        DrawerMenuItem("Inicio", Screen.Home.route, Icons.Default.Home),
        DrawerMenuItem("Mis citas", Screen.Appointments.route, Icons.Default.CalendarToday),
        DrawerMenuItem("Historial médico", Screen.MedicalHistory.route, Icons.Default.History),
        DrawerMenuItem("Perfil", Screen.Profile.route, Icons.Default.AccountCircle)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                modifier = Modifier.width(300.dp)
            ) {
                // Header Drawer
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PurplePrimary)
                        .padding(24.dp)
                ) {
                    // Circle Avatar with initials JP
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    ) {
                        Text(
                            text = "JP",
                            color = PurplePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Juan Pérez",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Paciente",
                        color = PurpleLight,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Drawer Navigation Items
                drawerItems.forEach { item ->
                    val isSelected = currentRoute == item.route

                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = item.title,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) PurplePrimary else TextPrimary
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = if (isSelected) PurplePrimary else TextSecondary
                            )
                        },
                        selected = isSelected,
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = PurpleLight,
                            unselectedContainerColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        onClick = {
                            scope.launch { drawerState.close() }
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route
                ) {
                    // 1. Home Screen
                    composable(Screen.Home.route) {
                        HomeScreen(
                            onOpenDrawer = {
                                scope.launch { drawerState.open() }
                            },
                            onDoctorSelected = { doctorId ->
                                navController.navigate(Screen.DoctorProfile.createRoute(doctorId))
                            }
                        )
                    }

                    // 2. Doctor Profile Screen
                    composable(
                        route = Screen.DoctorProfile.route,
                        arguments = listOf(navArgument("doctorId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val doctorId = backStackEntry.arguments?.getString("doctorId") ?: ""
                        DoctorProfileScreen(
                            doctorId = doctorId,
                            onBackClick = { navController.popBackStack() },
                            onBookAppointmentClick = { id ->
                                navController.navigate(Screen.BookAppointment.createRoute(id))
                            }
                        )
                    }

                    // 3. Appointment Screen
                    composable(
                        route = Screen.BookAppointment.route,
                        arguments = listOf(navArgument("doctorId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val doctorId = backStackEntry.arguments?.getString("doctorId") ?: ""
                        AppointmentScreen(
                            doctorId = doctorId,
                            onBackClick = { navController.popBackStack() },
                            onConfirmClick = { doctorName, date, time ->
                                navController.navigate(
                                    Screen.Confirmation.createRoute(doctorName, date, time)
                                ) {
                                    popUpTo(Screen.Home.route) { inclusive = false }
                                }
                            }
                        )
                    }

                    // 4. Confirmation Screen
                    composable(
                        route = Screen.Confirmation.route,
                        arguments = listOf(
                            navArgument("doctorName") { type = NavType.StringType },
                            navArgument("date") { type = NavType.StringType },
                            navArgument("time") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val doctorName = backStackEntry.arguments?.getString("doctorName") ?: ""
                        val date = backStackEntry.arguments?.getString("date") ?: ""
                        val time = backStackEntry.arguments?.getString("time") ?: ""

                        ConfirmationScreen(
                            doctorName = doctorName,
                            date = date,
                            time = time,
                            onViewAppointmentsClick = {
                                navController.navigate(Screen.Appointments.route) {
                                    popUpTo(Screen.Home.route) { inclusive = false }
                                }
                            }
                        )
                    }

                    // 5. Appointments Screen
                    composable(Screen.Appointments.route) {
                        AppointmentsScreen(
                            onOpenDrawer = {
                                scope.launch { drawerState.open() }
                            }
                        )
                    }

                    // 6. Medical History Screen
                    composable(Screen.MedicalHistory.route) {
                        MedicalHistoryScreen(
                            onOpenDrawer = {
                                scope.launch { drawerState.open() }
                            }
                        )
                    }

                    // 7. Profile Screen
                    composable(Screen.Profile.route) {
                        ProfileScreen(
                            onOpenDrawer = {
                                scope.launch { drawerState.open() }
                            }
                        )
                    }
                }
            }
        }
    }
}
