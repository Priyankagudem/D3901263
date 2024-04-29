package uk.ac.tees.mad.d3901263.screens.appointmenthistory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3901263.domain.Resource
import uk.ac.tees.mad.d3901263.domain.SalonAppointment
import uk.ac.tees.mad.d3901263.repository.FirestoreRepository
import javax.inject.Inject

@HiltViewModel
class AppointmentHistoryViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _appointmentHistoryStatus = Channel<HistoryResponseStatus>()
    val appointmentHistoryStatus = _appointmentHistoryStatus.receiveAsFlow()

    init {
        getAllSalonList()
    }

    private fun getAllSalonList() = viewModelScope.launch {
        firestoreRepository.getUserAppointments(Firebase.auth.currentUser?.uid!!).collect {
            when (it) {
                is Resource.Error -> {
                    _appointmentHistoryStatus.send(HistoryResponseStatus(isError = it.message))
                }

                is Resource.Loading -> {
                    _appointmentHistoryStatus.send(HistoryResponseStatus(isLoading = true))
                }

                is Resource.Success -> {
                    _appointmentHistoryStatus.send(HistoryResponseStatus(isSuccess = it.data))
                }
            }
        }
    }
}

data class HistoryResponseStatus(
    val isLoading: Boolean = false,
    val isSuccess: List<SalonAppointment>? = null,
    val isError: String? = null
)