package uk.ac.tees.mad.d3901263.screens.homescreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import uk.ac.tees.mad.d3901263.R
import uk.ac.tees.mad.d3901263.navigation.NavigationDestination

@Composable
fun HomeScreen(navController: NavHostController) {
    Text(text = "Home screen")
}

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes: Int = R.string.app_name
}