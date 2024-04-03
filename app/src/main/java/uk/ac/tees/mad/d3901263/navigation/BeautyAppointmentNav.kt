package uk.ac.tees.mad.d3901263.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.d3901263.screens.OnboardingDestination
import uk.ac.tees.mad.d3901263.screens.OnboardingScreen
import uk.ac.tees.mad.d3901263.screens.homescreen.HomeDestination
import uk.ac.tees.mad.d3901263.screens.homescreen.HomeScreen
import uk.ac.tees.mad.d3901263.screens.splash.SplashDestination
import uk.ac.tees.mad.d3901263.screens.splash.SplashScreen

@Composable
fun BeautyAppointmentNav() {
    val navController = rememberNavController()

    val context = LocalContext.current
    val destinationScreen =
        if (isOnboardingFinished(context)) HomeDestination.route else OnboardingDestination.route

    NavHost(navController = navController, startDestination = SplashDestination.route) {
        composable(SplashDestination.route) {
            SplashScreen(
                onFinish = {
                    navController.popBackStack()
                    navController.navigate(destinationScreen)
                }
            )
        }
        composable(OnboardingDestination.route) {
            OnboardingScreen(navController = navController)
        }
        composable(HomeDestination.route) {
            HomeScreen(/*navController = navController*/)
        }
    }
}

private fun isOnboardingFinished(context: Context): Boolean {
    val sharedPreference = context.getSharedPreferences("onboarding", Context.MODE_PRIVATE)
    return sharedPreference.getBoolean("isFinished", false)
}

interface NavigationDestination {
    val route: String
    val titleRes: Int
}