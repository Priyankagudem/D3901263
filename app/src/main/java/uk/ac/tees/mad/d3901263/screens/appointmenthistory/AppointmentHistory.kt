package uk.ac.tees.mad.d3901263.screens.appointmenthistory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import uk.ac.tees.mad.d3901263.R
import uk.ac.tees.mad.d3901263.navigation.Navigation
import uk.ac.tees.mad.d3901263.screens.appointmenthistory.viewmodel.AppointmentHistoryViewModel
import uk.ac.tees.mad.d3901263.screens.profile.ProfileHeader
import uk.ac.tees.mad.d3901263.ui.theme.primaryPink

@Composable
fun AppointmentHistoryScreen(
    onBack: () -> Unit
) {
    val viewModel: AppointmentHistoryViewModel = hiltViewModel()
    val historyFetchState by viewModel.appointmentHistoryStatus.collectAsState(initial = null)

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        ProfileHeader(
            onBack = onBack,
            title = "Appointment History"
        )
        Column(modifier = Modifier.padding(vertical = 24.dp)) {
            if (historyFetchState?.isLoading == true) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                if (historyFetchState?.isSuccess.isNullOrEmpty()) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp), horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "No appointment history")
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(historyFetchState?.isSuccess!!) { appointment ->
                            Card {
                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Box(
                                            Modifier
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(primaryPink.copy(0.2f))
                                        ) {
                                            Text(
                                                text = "Slot - ${appointment.slot}",
                                                fontSize = 17.sp,
                                                fontWeight = FontWeight.Medium,
                                                modifier = Modifier.padding(
                                                    horizontal = 12.dp,
                                                    vertical = 6.dp
                                                ),
                                                color = primaryPink
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Appointment Id- ", fontSize = 15.sp)
                                        Text(
                                            text = appointment.appointmentId,
                                            fontSize = 19.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Salon details- ", fontSize = 15.sp)
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                text = appointment.salonName,
                                                fontSize = 22.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                            Text(
                                                text = appointment.salonAddress,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

object AppointmentHistory : Navigation {
    override val route = "appo_history"
    override val titleRes: Int = R.string.app_name
}