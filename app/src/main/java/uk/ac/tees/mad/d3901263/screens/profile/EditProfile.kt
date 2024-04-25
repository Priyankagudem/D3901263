package uk.ac.tees.mad.d3901263.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import uk.ac.tees.mad.d3901263.R
import uk.ac.tees.mad.d3901263.navigation.Navigation

@Composable
fun EditProfileScreen(
    onBack: () -> Unit
) {
    Column (Modifier.fillMaxSize()){
        ProfileHeader(
            onBack = onBack,
            title = "Edit profile"
        )
    }
}

object EditProfile : Navigation {
    override val route = "edit_profile"
    override val titleRes: Int = R.string.app_name
}