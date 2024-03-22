package uk.ac.tees.mad.d3901263.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.d3901263.screens.homescreen.HomeDestination
import uk.ac.tees.mad.d3901263.screens.homescreen.HomeScreen
import uk.ac.tees.mad.d3901263.screens.splash.SplashDestination
import uk.ac.tees.mad.d3901263.screens.splash.SplashScreen

@Composable
fun BeautyAppointmentNav() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = SplashDestination.route) {
        composable(SplashDestination.route) {
            SplashScreen(navController = (navController))
        }
        composable(HomeDestination.route) {
            HomeScreen(navController = navController)
        }
    }
}

interface NavigationDestination {
    val route: String
    val titleRes: Int
}