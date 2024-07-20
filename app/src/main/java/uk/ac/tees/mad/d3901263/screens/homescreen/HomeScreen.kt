package uk.ac.tees.mad.d3901263.screens.homescreen

import android.Manifest
import android.location.Location
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Timelapse
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import uk.ac.tees.mad.d3901263.ApplicationViewModel
import uk.ac.tees.mad.d3901263.LocationRepository
import uk.ac.tees.mad.d3901263.R
import uk.ac.tees.mad.d3901263.domain.Salon
import uk.ac.tees.mad.d3901263.navigation.Navigation
import uk.ac.tees.mad.d3901263.screens.homescreen.viewmodel.HomeViewModel
import uk.ac.tees.mad.d3901263.ui.theme.darkGrey
import uk.ac.tees.mad.d3901263.ui.theme.primaryPink
import uk.ac.tees.mad.d3901263.ui.theme.smokeWhite

/*  Home Screen:
        Salon recommendations based on user preferences.
        Quick access buttons for popular services.
*/

val servicesList = listOf<Service>(
    Service(
        name = "Haircuts",
        iconRes = R.drawable.haircut
    ),
    Service(
        name = "Make up",
        iconRes = R.drawable.cosmetic_brush_46
    ),
    Service(
        name = "Shaving",
        iconRes = R.drawable._321913
    ),
    Service(
        name = "Massage",
        iconRes = R.drawable.hand_cream
    ),
    Service(
        name = "Facial",
        iconRes = R.drawable.facial
    ),
)

data class Service(
    val name: String,
    val iconRes: Int
)

@Composable
fun HomeScreen(onItemClick: (String) -> Unit, onProfileClick: () -> Unit) {
    val viewModel: HomeViewModel = hiltViewModel()
    val salonListStatus by viewModel.salonListStatus.collectAsState(initial = null)
    var salonList by remember {
        mutableStateOf<List<Salon>>(emptyList())
    }
    val context = LocalContext.current
    LaunchedEffect(salonListStatus?.isSuccess) {
        salonListStatus?.isSuccess?.let {
            salonList = it
        }
    }

    var searchValue by remember {
        mutableStateOf("")
    }

    LaunchedEffect(searchValue) {
        salonList = if (searchValue.isNotEmpty()) {
            viewModel.salonList.filter { sal ->
                sal.name.lowercase().contains(searchValue.lowercase())
            }
        } else {
            viewModel.salonList
        }
        println(salonList)
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(Color.White)
    ) {

        HomeHeader(onProfileClick = onProfileClick)

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SearchRow(
                    onValueChange = {
                        searchValue = it

                    },
                    searchValue = searchValue
                )

//                Spacer(modifier = Modifier.height(20.dp))

//                Column(modifier = Modifier.fillMaxWidth()) {
//                    Row {
//                        Text(
//                            text = "Services",
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//                    Spacer(modifier = Modifier.height(10.dp))
//                    ServiceRow()
//                }

                Spacer(modifier = Modifier.height(20.dp))

                //Salon List
                Row {
                    Text(
                        text = "Top Rated Salons",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            if (salonListStatus?.isLoading == true) {
                item {
                    CircularProgressIndicator()
                }
            } else {
                items(salonList) { salon ->
                    SalonItemCard(
                        salon = salon,
                        onClick = { onItemClick(salon.id) },
                        onLike = {
                            viewModel.addItemToFavorite(salon, context)
                        }
                    )
                }
            }
        }

    }
}

@Composable
fun ServiceRow() {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(servicesList) { service ->
            ServiceIconBox(service)
        }
    }
}

@Composable
fun SalonItemCard(salon: Salon, onClick: () -> Unit, onLike: () -> Unit, unLike: Boolean = false) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, primaryPink, RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
    ) {
        Box(
            modifier = Modifier
                .height(250.dp)

        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).crossfade(true)
                    .data(salon.imageUrl).build(),
                contentDescription = "Salon image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                Modifier
                    .fillMaxHeight()
                    .padding(10.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Column(Modifier.fillMaxHeight(), horizontalAlignment = Alignment.End) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(50.dp)
                            .background(if (unLike) primaryPink else Color.White.copy(alpha = 0.9f))
                            .clickable {
                                onLike()
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (unLike) smokeWhite else darkGrey,
                            modifier = Modifier
                                .padding(10.dp)
                                .align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.White)
                            .padding(vertical = 4.dp, horizontal = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = "",
                            tint = Color.Yellow
                        )
                        Text(text = "${salon.rating}")
                    }
                }
            }
        }
        HorizontalDivider(color = primaryPink)
        Text(
            text = salon.name,
            modifier = Modifier.padding(start = 12.dp, top = 6.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Column(
            Modifier
                .padding(start = 12.dp, bottom = 4.dp, end = 12.dp)
                .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = null,
                    tint = primaryPink
                )
                Text(text = salon.address, modifier = Modifier.padding(4.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.Timelapse,
                    contentDescription = null,
                    tint = primaryPink
                )
                Text(
                    text = "${salon.openTiming}-${salon.closeTiming}",
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@Composable
fun SearchRow(
    onValueChange: (String) -> Unit,
    searchValue: String
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            value = searchValue,
            onValueChange = onValueChange,
            placeholder = {
                Text(text = "Search Salon, Specialist...")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "search",
                    tint = primaryPink
                )
            },
            singleLine = true
        )
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(primaryPink)
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "filter",
                modifier = Modifier.align(Alignment.Center),
                tint = Color.White
            )
        }
    }
}

@Composable
fun ServiceIconBox(service: Service) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(primaryPink.copy(alpha = 0.2f))
        ) {
            Image(
                painter = painterResource(id = service.iconRes),
                contentDescription = service.name,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = service.name, fontWeight = FontWeight.Bold)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeHeader(
    onProfileClick: () -> Unit
) {
    val context = LocalContext.current
    val applicationViewModel: ApplicationViewModel = hiltViewModel()
    val locationPermissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    val locationPermissionsState = rememberMultiplePermissionsState(
        locationPermissions
    )
    val activity = (context as ComponentActivity)
    val locationManager = LocationRepository(context, activity)
    val isGpsEnabled = locationManager.gpsStatus.collectAsState(initial = false)
    val location = Location("MyLocationProvider")

    val locationState =
        applicationViewModel.locationFlow.collectAsState(
            initial = location.apply {
                latitude = 51.509865
                longitude = -0.118092
            }
        )
    var locationValue by remember {
        mutableStateOf("Select Location")
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Location",
                modifier = Modifier.padding(6.dp),
                color = MaterialTheme.colorScheme.secondary
            )
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
                if (locationPermissionsState.allPermissionsGranted) {
                    if (!isGpsEnabled.value) {
                        locationManager.checkGpsSettings()
                        println("Not enabled")
                    } else {
                        locationValue = locationManager.getAddressFromCoordinate(
                            latitude = locationState.value.latitude,
                            longitude = locationState.value.longitude
                        )

                    }
                } else {
                    println("Permisson not got")

                    locationPermissionsState.launchMultiplePermissionRequest()
                }
            }) {
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = null,
                    tint = primaryPink
                )
                Text(
                    text = locationValue,
                    color = Color.Black,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }
        IconButton(
            onClick = onProfileClick,
            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.tertiary),
            modifier = Modifier.clip(CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Settings"
            )
        }
    }
}

object Home : Navigation {
    override val route = "home"
    override val titleRes: Int = R.string.app_name
}