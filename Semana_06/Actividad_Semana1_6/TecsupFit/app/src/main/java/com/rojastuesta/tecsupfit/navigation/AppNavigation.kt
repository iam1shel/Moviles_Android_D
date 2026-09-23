package com.rojastuesta.tecsupfit.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rojastuesta.tecsupfit.components.BottomNavigationBar
import com.rojastuesta.tecsupfit.data.SampleData
import com.rojastuesta.tecsupfit.model.Reservation
import com.rojastuesta.tecsupfit.model.ReservationStatus
import com.rojastuesta.tecsupfit.screens.ClassDetailScreen
import com.rojastuesta.tecsupfit.screens.ConfirmationScreen
import com.rojastuesta.tecsupfit.screens.HomeScreen
import com.rojastuesta.tecsupfit.screens.ProfileScreen
import com.rojastuesta.tecsupfit.screens.ReservationsScreen
import com.rojastuesta.tecsupfit.screens.RoutinesScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Top-level local state for user's reservations
    var userReservations by remember { mutableStateOf(SampleData.initialReservations) }

    // Define main bottom bar routes
    val mainRoutes = listOf(
        Screen.Home.route,
        Screen.Reservations.route,
        Screen.Routines.route,
        Screen.Profile.route
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in mainRoutes) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = { targetRoute ->
                        navController.navigate(targetRoute) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. HOME SCREEN
            composable(route = Screen.Home.route) {
                HomeScreen(
                    onClassClick = { classId ->
                        navController.navigate(Screen.ClassDetail.createRoute(classId))
                    }
                )
            }

            // 2. CLASS DETAIL SCREEN
            composable(
                route = Screen.ClassDetail.route,
                arguments = listOf(navArgument("classId") { type = NavType.StringType })
            ) { backStackEntry ->
                val classId = backStackEntry.arguments?.getString("classId") ?: ""
                ClassDetailScreen(
                    classId = classId,
                    onBackClick = { navController.popBackStack() },
                    onReserveClick = { reservedClassId ->
                        // Add reservation if not already added
                        val gymClass = SampleData.getClassById(reservedClassId)
                        val newReservation = Reservation(
                            id = "res_${System.currentTimeMillis()}",
                            className = gymClass.name,
                            timeInfo = "Hoy, ${gymClass.time}",
                            room = gymClass.room,
                            status = ReservationStatus.CONFIRMADA
                        )
                        if (userReservations.none { it.className == gymClass.name && it.status == ReservationStatus.CONFIRMADA }) {
                            userReservations = listOf(newReservation) + userReservations
                        }

                        // Navigate to confirmation screen
                        navController.navigate(Screen.Confirmation.createRoute(reservedClassId))
                    }
                )
            }

            // 3. CONFIRMATION SCREEN
            composable(
                route = Screen.Confirmation.route,
                arguments = listOf(navArgument("classId") { type = NavType.StringType })
            ) { backStackEntry ->
                val classId = backStackEntry.arguments?.getString("classId") ?: ""
                ConfirmationScreen(
                    classId = classId,
                    onViewReservationsClick = {
                        navController.navigate(Screen.Reservations.route) {
                            popUpTo(Screen.Home.route) { inclusive = false }
                        }
                    }
                )
            }

            // 4. RESERVATIONS SCREEN
            composable(route = Screen.Reservations.route) {
                ReservationsScreen(
                    reservations = userReservations
                )
            }

            // 5. ROUTINES SCREEN
            composable(route = Screen.Routines.route) {
                RoutinesScreen()
            }

            // 6. PROFILE SCREEN
            composable(route = Screen.Profile.route) {
                ProfileScreen()
            }
        }
    }
}
