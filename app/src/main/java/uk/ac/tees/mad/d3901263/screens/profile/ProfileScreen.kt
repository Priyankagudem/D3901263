package uk.ac.tees.mad.d3901263.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.d3901263.R
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
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ProfileHeader(
            onBack = {
                navController.navigateUp()
            },
            title = "Profile"
        )
        Spacer(modifier = Modifier.height(20.dp))
        ProfileCard(onClick = {})
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
        modifier = Modifier.fillMaxWidth()
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
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = primaryPink
            )
        }
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
    onClick: () -> Unit
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
            Image(
                painter = painterResource(id = R.drawable.salon1),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
//            AsyncImage(
//                model = ImageRequest
//                    .Builder(LocalContext.current)
//                    .crossfade(true)
//                    .data(R.drawable.salon1),
//                contentDescription = null,
//                contentScale = ContentScale.Crop
//            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = "Profile name",
                color = smokeWhite,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Email", color = smokeWhite, fontSize = 16.sp)
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