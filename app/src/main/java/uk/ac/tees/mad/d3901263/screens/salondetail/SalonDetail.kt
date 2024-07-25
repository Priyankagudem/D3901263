package uk.ac.tees.mad.d3901263.screens.salondetail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Timelapse
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import uk.ac.tees.mad.d3901263.R
import uk.ac.tees.mad.d3901263.domain.Salon
import uk.ac.tees.mad.d3901263.navigation.Navigation
import uk.ac.tees.mad.d3901263.ui.theme.darkGrey
import uk.ac.tees.mad.d3901263.ui.theme.primaryPink
import uk.ac.tees.mad.d3901263.ui.theme.smokeWhite

@Composable
fun SalonDetail(
    onBack: () -> Unit
) {
    val viewModel = hiltViewModel<SalonDetailViewModel>()
    val fetchSalonState by viewModel.fetchSalonStatus.collectAsState(initial = null)
    val bookAppointmentStatus by viewModel.bookAppointmentStatus.collectAsState(initial = null)
    val salon = fetchSalonState?.isSuccess
    LaunchedEffect(Unit) {
        viewModel.getSalonDetail()
    }
    var showBookingScreen by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    LaunchedEffect(bookAppointmentStatus?.isSuccess) {
        bookAppointmentStatus?.isSuccess?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            showBookingScreen = false
        }
    }
    LaunchedEffect(bookAppointmentStatus?.isError) {
        bookAppointmentStatus?.isSuccess?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }


    Column(Modifier.fillMaxSize()) {
        if (fetchSalonState?.isLoading == true) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = primaryPink.copy(
                        alpha = 0.2f
                    )
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(250.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).crossfade(true)
                            .data(salon?.imageUrl).build(),
                        contentDescription = "Salon image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth()
                    )
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .padding(16.dp),
                        colors = IconButtonDefaults.iconButtonColors(containerColor = smokeWhite)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }

                    Row(
                        Modifier
                            .fillMaxHeight()
                            .padding(10.dp)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Column(Modifier.fillMaxHeight(), horizontalAlignment = Alignment.End) {

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
                                Text(text = "${salon?.rating}")
                            }
                        }
                    }
                }
            }
            Column(Modifier.padding(16.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = salon?.name ?: "",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(45.dp)
                            .background(primaryPink)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.heart),
                            contentDescription = "Favorite",
                            Modifier
                                .padding(10.dp)
                                .align(Alignment.Center),
                            tint = smokeWhite
                        )
                    }
                }


            }
            if (showBookingScreen) {
                BookingScreen(
                    isLoading = bookAppointmentStatus?.isLoading == true,
                    onBook = viewModel::bookAppointment
                )
            } else {
                SalonDetailComponent(salon = salon, onBook = {
                    showBookingScreen = true
                })
            }
        }
    }
}

@Composable
fun SalonDetailComponent(
    salon: Salon?,
    onBook: () -> Unit
) {
    Column(
        Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = salon?.description ?: "",
            fontSize = 15.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 4.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Rounded.LocationOn,
                contentDescription = null,
                tint = primaryPink
            )
            Text(text = salon?.address ?: "", modifier = Modifier.padding(4.dp))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Rounded.Timelapse,
                contentDescription = null,
                tint = primaryPink
            )
            Text(
                text = "${salon?.openTiming ?: ""} - ${salon?.closeTiming ?: ""}",
                modifier = Modifier.padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Services offered:",
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),

        ) {
            salon?.servicesOffered?.forEachIndexed { index, s ->

                    Text(
                        text = "${index+1}. $s",
                        fontSize = 16.sp,
                        color = darkGrey,
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    )

            }
        }

        Button(
            onClick = onBook,
            colors = ButtonDefaults.buttonColors(containerColor = primaryPink),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(50.dp)
        ) {
            Text(text = "Book appointment")
        }
    }
}

@Composable
fun BookingScreen(
    isLoading: Boolean,
    onBook: (String) -> Unit
) {
    val availableSlots = remember {
        mutableStateListOf(
            "9:00 AM",
            "10:00 AM",
            "11:00 AM",
            "12:00 AM",
            "01:00 PM",
            "02:00 PM",
            "03:00 PM",
            "04:00 PM"
        )
    }
    val selectedSlot = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp), horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Book an appointment slot", fontWeight = FontWeight.Medium)
        }
        Text(text = "Select a slot:")
        Spacer(modifier = Modifier.height(8.dp))
        SlotList(
            modifier = Modifier.weight(1f),
            slots = availableSlots,
            selectedSlot = selectedSlot.value,
            onSlotSelected = { slot -> selectedSlot.value = slot }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onBook(selectedSlot.value) },
            enabled = selectedSlot.value.isNotBlank(),
            colors = ButtonDefaults.buttonColors(primaryPink),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = smokeWhite)
            } else {
                Text(text = "Book Slot")
            }
        }
    }
}

@Composable
fun SlotList(
    modifier: Modifier = Modifier,
    slots: List<String>,
    selectedSlot: String,
    onSlotSelected: (String) -> Unit
) {
    LazyColumn(modifier) {
        items(slots) { slot ->
            SlotItem(
                slot = slot,
                isSelected = slot == selectedSlot,
                onSlotSelected = { onSlotSelected(slot) }
            )
        }
    }
}

@Composable
fun SlotItem(
    slot: String,
    isSelected: Boolean,
    onSlotSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSlotSelected() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
            colors = RadioButtonDefaults.colors(selectedColor = primaryPink)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = slot)
    }
}

object SalonDetailDestination : Navigation {
    override val route = "salon"
    override val titleRes: Int = R.string.app_name
    const val salonIdArg = "placeId"
    val routeWithArgs = "$route/{$salonIdArg}"
}