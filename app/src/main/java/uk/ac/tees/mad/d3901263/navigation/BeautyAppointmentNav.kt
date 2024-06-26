package uk.ac.tees.mad.d3901263.navigation

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3901263.screens.Onboarding
import uk.ac.tees.mad.d3901263.screens.OnboardingScreen
import uk.ac.tees.mad.d3901263.screens.appointmenthistory.AppointmentHistory
import uk.ac.tees.mad.d3901263.screens.appointmenthistory.AppointmentHistoryScreen
import uk.ac.tees.mad.d3901263.screens.authentication.Login
import uk.ac.tees.mad.d3901263.screens.authentication.LoginScreen
import uk.ac.tees.mad.d3901263.screens.authentication.Register
import uk.ac.tees.mad.d3901263.screens.authentication.RegisterScreen
import uk.ac.tees.mad.d3901263.screens.homescreen.Home
import uk.ac.tees.mad.d3901263.screens.homescreen.HomeScreen
import uk.ac.tees.mad.d3901263.screens.liked.LikedItem
import uk.ac.tees.mad.d3901263.screens.liked.LikedItemScreen
import uk.ac.tees.mad.d3901263.screens.profile.EditProfile
import uk.ac.tees.mad.d3901263.screens.profile.EditProfileScreen
import uk.ac.tees.mad.d3901263.screens.profile.Profile
import uk.ac.tees.mad.d3901263.screens.profile.ProfileScreen
import uk.ac.tees.mad.d3901263.screens.salondetail.SalonDetail
import uk.ac.tees.mad.d3901263.screens.salondetail.SalonDetailDestination
import uk.ac.tees.mad.d3901263.screens.splash.Splash
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
            Home.route
        } else if (isOnboardingFinished(context) && currentUser == null) {
            Login.route
        } else {
            Onboarding.route
        }

    NavHost(navController = navController, startDestination = Splash.route) {
        composable(Splash.route) {
            SplashScreen(
                onFinish = {
                    navController.popBackStack()
                    navController.navigate(destinationScreen)
                }
            )
        }
        composable(Onboarding.route) {
            OnboardingScreen(navController = navController)
        }
        composable(Home.route) {
            HomeScreen(
                onItemClick = {
                    navController.navigate(SalonDetailDestination.route + "/" + it)
                },
                onProfileClick = {
                    navController.navigate(Profile.route)
                }
            )
        }

        composable(Login.route) {
            LoginScreen(
                onLoginComplete = {
                    context.makeToast("Login success")
                    navController.navigate(Home.route)
                },
                onRegisterClick = { navController.navigate(Register.route) })
        }

        composable(Register.route) {
            RegisterScreen(
                onRegisterComplete = {
                    context.makeToast("Register success")
                    navController.navigate(Home.route)
                },
                onLoginClick = { navController.navigate(Login.route) })
        }
        composable(
            route = SalonDetailDestination.routeWithArgs,
            arguments = listOf(
                navArgument(SalonDetailDestination.salonIdArg) {
                    type = NavType.StringType
                }
            )
        ) {
            SalonDetail(onBack = { navController.navigateUp() })
        }

        composable(Profile.route) {
            ProfileScreen(
                navController = navController,
                logout = {
                    scope.launch {
                        firebaseAuth.signOut()
                        context.makeToast("Sign out success")
                        delay(500L)
                        navController.navigate(Login.route)
                    }
                }
            )
        }

        composable(EditProfile.route) {
            EditProfileScreen(
                onBack = { navController.navigateUp() }
            )
        }

        composable(AppointmentHistory.route) {
            AppointmentHistoryScreen(onBack = { navController.navigateUp() })
        }
        composable(LikedItem.route) {
            LikedItemScreen(
                onBack = { navController.navigateUp() },
                onItemClick = {
                    navController.navigate(SalonDetailDestination.route + "/" + it)
                }
            )
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


interface Navigation {
    val route: String
    val titleRes: Int
}