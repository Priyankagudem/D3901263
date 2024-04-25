package uk.ac.tees.mad.d3901263.screens.liked

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import uk.ac.tees.mad.d3901263.R
import uk.ac.tees.mad.d3901263.navigation.Navigation
import uk.ac.tees.mad.d3901263.screens.profile.ProfileHeader

@Composable
fun LikedItemScreen(
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        ProfileHeader(
            onBack = onBack,
            title = "Liked Salons"
        )
    }
}

object LikedItem : Navigation {
    override val route = "liked"
    override val titleRes: Int = R.string.app_name
}