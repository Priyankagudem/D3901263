package uk.ac.tees.mad.d3901263.screens.salondetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3901263.domain.Resource
import uk.ac.tees.mad.d3901263.domain.Salon
import uk.ac.tees.mad.d3901263.repository.FirestoreRepository
import javax.inject.Inject

@HiltViewModel
class SalonDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {
    private val salonId: String = checkNotNull(savedStateHandle[SalonDetailDestination.salonIdArg])

    private val _fetchSalonStatus = Channel<FetchSalonStatus>()
    val fetchSalonStatus = _fetchSalonStatus.receiveAsFlow()

    private val _bookAppointmentStatus = Channel<ResponseStatus>()
    val bookAppointmentStatus = _bookAppointmentStatus.receiveAsFlow()

    fun getSalonDetail() = viewModelScope.launch {
        firestoreRepository.getSalonDetails(salonId).collect {
            when (it) {
                is Resource.Error -> {
                    _fetchSalonStatus.send(FetchSalonStatus(isError = it.message))
                }

                is Resource.Loading -> {
                    _fetchSalonStatus.send(FetchSalonStatus(isLoading = true))
                }

                is Resource.Success -> {
                    _fetchSalonStatus.send(FetchSalonStatus(isSuccess = it.data))
                }
            }
        }
    }

    fun bookAppointment(selectedSlot: String) = viewModelScope.launch {
        firestoreRepository.bookAppointment(salonId, Firebase.auth.currentUser?.uid!!, selectedSlot)
            .collect {
                when (it) {
                    is Resource.Error -> {
                        _bookAppointmentStatus.send(ResponseStatus(isError = it.message))
                    }

                    is Resource.Loading -> {
                        _bookAppointmentStatus.send(ResponseStatus(isLoading = true))
                    }

                    is Resource.Success -> {
                        _bookAppointmentStatus.send(ResponseStatus(isSuccess = it.data))
                    }
                }
            }
    }
}

data class FetchSalonStatus(
    val isLoading: Boolean = false,
    val isSuccess: Salon? = null,
    val isError: String? = null
)

data class ResponseStatus(
    val isLoading: Boolean = false,
    val isSuccess: String? = null,
    val isError: String? = null
)