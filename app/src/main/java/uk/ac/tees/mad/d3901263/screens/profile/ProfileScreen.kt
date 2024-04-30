package uk.ac.tees.mad.d3901263.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import uk.ac.tees.mad.d3901263.R
import uk.ac.tees.mad.d3901263.domain.FirestoreUser
import uk.ac.tees.mad.d3901263.navigation.Navigation
import uk.ac.tees.mad.d3901263.screens.appointmenthistory.AppointmentHistory
import uk.ac.tees.mad.d3901263.screens.liked.LikedItem
import uk.ac.tees.mad.d3901263.ui.theme.darkGrey
import uk.ac.tees.mad.d3901263.ui.theme.lightGrey
import uk.ac.tees.mad.d3901263.ui.theme.primaryPink
import uk.ac.tees.mad.d3901263.ui.theme.smokeWhite

@Composable
fun ProfileScreen(
    navController: NavHostController,
    logout: () -> Unit
) {
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val getUserState by profileViewModel.currentUserState.collectAsState(initial = null)
    val user = getUserState?.data?.item
    LaunchedEffect(Unit) {
        profileViewModel.getUserInformation()
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ProfileHeader(
            onBack = {
                navController.navigateUp()
            },
            title = "Settings"
        )
        Spacer(modifier = Modifier.height(20.dp))
        ProfileCard(onClick = {}, user = user)
        ProfileItem(
            text = "Your profile",
            iconVector = Icons.Outlined.PersonOutline,
            onClick = {
                navController.navigate(EditProfile.route)
            }
        )
        ProfileItem(
            text = "Appointment history",
            iconVector = Icons.Outlined.History,
            onClick = {
                navController.navigate(AppointmentHistory.route)
            }
        )
        ProfileItem(
            text = "Saved",
            iconVector = Icons.Outlined.FavoriteBorder,
            onClick = {
                navController.navigate(LikedItem.route)
            }
        )
        ProfileItem(
            text = "Log out",
            iconVector = Icons.AutoMirrored.Outlined.Logout,
            onClick = logout
        )
    }
}

@Composable
fun ProfileItem(
    text: String,
    iconVector: ImageVector,
    onClick: () -> Unit
) {
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clickable {
                onClick()
            }

    ) {
        Icon(
            imageVector = iconVector,
            contentDescription = null,
            tint = primaryPink,
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, fontSize = 18.sp)
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = primaryPink
        )


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileHeader(
    onBack: () -> Unit,
    title: String
) {
    CenterAlignedTopAppBar(title = { Text(text = title) }, navigationIcon = {
        IconButton(
            onClick = onBack,
            modifier = Modifier.border(1.dp, lightGrey, CircleShape)
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
        }
    })
}

@Composable
fun ProfileCard(
    onClick: () -> Unit,
    user: FirestoreUser.UserDetail?
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(darkGrey),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .padding(12.dp)
                .clip(CircleShape)
        ) {
            if (user?.image == null) {

                Image(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            } else {
                AsyncImage(
                    model = ImageRequest
                        .Builder(LocalContext.current)
                        .crossfade(true)
                        .data(user.image)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = "${user?.name}",
                color = smokeWhite,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "${user?.email}", color = smokeWhite, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = primaryPink
            )
        }
    }
}

object Profile : Navigation {
    override val route = "profile"
    override val titleRes: Int = R.string.app_name
}