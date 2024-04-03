package uk.ac.tees.mad.d3901263.screens.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Timelapse
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.tees.mad.d3901263.R
import uk.ac.tees.mad.d3901263.navigation.NavigationDestination
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
fun HomeScreen(/*navController: NavHostController*/) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(Color.White)
    ) {

        HomeHeader()

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SearchRow()

                Spacer(modifier = Modifier.height(20.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Row {
                        Text(
                            text = "Services",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    ServiceRow()
                }

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

            items(SalonList) { salon ->
                SalonItemCard(salon = salon)
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalonItemCard(salon: Salon) {
    Card(
        onClick = {},
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(
                alpha = 0.2f
            )
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Box(
            modifier = Modifier
                .height(250.dp)
        ) {
            Image(
                painter = painterResource(id = salon.imageRes),
                contentDescription = "",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
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
                            .background(smokeWhite.copy(alpha = 0.9f))
                    ) {
                        Icon(
                            painterResource(id = R.drawable.heart),
                            contentDescription = "Favorite",
                            Modifier.padding(10.dp).align(Alignment.Center)
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
        Text(
            text = salon.name,
            modifier = Modifier.padding(start = 12.dp, top = 6.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            Modifier
                .padding(start = 12.dp, bottom = 4.dp, end = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(text = salon.address, modifier = Modifier.padding(4.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.Timelapse,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(text = salon.timing, modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@Composable
fun SearchRow() {
    Row(
        Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            value = "",
            onValueChange = {},
            enabled = false,
            placeholder = {
                Text(text = "Search Salon, Specialist...")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "search",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary)
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
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
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

@Composable
fun HomeHeader() {
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "New Delhi, India",
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
            onClick = { /*TODO*/ },
            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.tertiary),
            modifier = Modifier.clip(CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notification"
            )
        }
    }
}

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes: Int = R.string.app_name
}

data class Salon(
    val name: String,
    val address: String,
    val rating: Double,
    val imageRes: Int,
    val timing: String
)

val SalonList = listOf<Salon>(
    Salon(
        name = "Glamour Heaven",
        address = "Sector-51, Noida",
        rating = 4.5,
        imageRes = R.drawable.salon1,
        timing = "10am - 11pm"
    ),
    Salon(
        name = "Glamour Heaven",
        address = "Sector-51, Noida",
        rating = 4.5,
        imageRes = R.drawable.salon2,
        timing = "10am - 11pm"
    ),
    Salon(
        name = "Glamour Heaven",
        address = "Sector-51, Noida",
        rating = 4.5,
        imageRes = R.drawable.salon3,
        timing = "10am - 11pm"
    ),
    Salon(
        name = "Glamour Heaven",
        address = "Sector-51, Noida",
        rating = 4.5,
        imageRes = R.drawable.salon2,
        timing = "10am - 11pm"
    ),
    Salon(
        name = "Glamour Heaven",
        address = "Sector-51, Noida",
        rating = 4.5,
        imageRes = R.drawable.salon1,
        timing = "10am - 11pm"
    ),
)