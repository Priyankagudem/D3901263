package uk.ac.tees.mad.d3901263.screens.appointmenthistory

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import uk.ac.tees.mad.d3901263.R
import uk.ac.tees.mad.d3901263.navigation.Navigation
import uk.ac.tees.mad.d3901263.screens.profile.ProfileHeader

@Composable
fun AppointmentHistoryScreen(
    onBack: () -> Unit
) {
    Column (Modifier.fillMaxSize()){
        ProfileHeader(
            onBack = onBack,
            title = "Appointment History"
        )
    }
}

object AppointmentHistory : Navigation {
    override val route = "appo_history"
    override val titleRes: Int = R.string.app_name
}