package uk.ac.tees.mad.d3901263.navigation

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3901263.screens.OnboardingDestination
import uk.ac.tees.mad.d3901263.screens.OnboardingScreen
import uk.ac.tees.mad.d3901263.screens.authentication.LoginDestination
import uk.ac.tees.mad.d3901263.screens.authentication.LoginScreen
import uk.ac.tees.mad.d3901263.screens.authentication.RegisterDestination
import uk.ac.tees.mad.d3901263.screens.authentication.RegisterScreen
import uk.ac.tees.mad.d3901263.screens.homescreen.HomeDestination
import uk.ac.tees.mad.d3901263.screens.homescreen.HomeScreen
import uk.ac.tees.mad.d3901263.screens.splash.SplashDestination
import uk.ac.tees.mad.d3901263.screens.splash.SplashScreen

@Composable
fun BeautyAppointmentNav() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val firebaseAuth = Firebase.auth
    val currentUser = firebaseAuth.currentUser

    val context = LocalContext.current

    val destinationScreen =
        if (isOnboardingFinished(context) && currentUser != null) {
            HomeDestination.route
        } else if (isOnboardingFinished(context) && currentUser == null) {
            LoginDestination.route
        } else {
            OnboardingDestination.route
        }

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
            HomeScreen(onSignOut = {
                scope.launch {
                    firebaseAuth.signOut()
                    context.makeToast("Sign out success")
                    delay(500L)
                    navController.navigate(LoginDestination.route)
                }
            })
        }

        composable(LoginDestination.route) {
            LoginScreen(
                onLoginComplete = {
                    context.makeToast("Login success")
                    navController.navigate(HomeDestination.route)
                },
                onRegisterClick = { navController.navigate(RegisterDestination.route) })
        }

        composable(RegisterDestination.route) {
            RegisterScreen(
                onRegisterComplete = {
                    context.makeToast("Register success")
                    navController.navigate(HomeDestination.route)
                },
                onLoginClick = { navController.navigate(LoginDestination.route) })
        }
    }
}

private fun isOnboardingFinished(context: Context): Boolean {
    val sharedPreference = context.getSharedPreferences("onboarding", Context.MODE_PRIVATE)
    return sharedPreference.getBoolean("isFinished", false)
}


fun Context.makeToast(text: String) {
    Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
}


interface NavigationDestination {
    val route: String
    val titleRes: Int
}