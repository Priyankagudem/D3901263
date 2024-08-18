package uk.ac.tees.mad.d3901263.screens.homescreen.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3901263.database.LikedItemEntity
import uk.ac.tees.mad.d3901263.database.LikedItemRepository
import uk.ac.tees.mad.d3901263.domain.Resource
import uk.ac.tees.mad.d3901263.domain.Salon
import uk.ac.tees.mad.d3901263.repository.FirestoreRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val likedItemRepository: LikedItemRepository
) : ViewModel() {

    private val _salonListStatus = Channel<ResponseStatus>()
    val salonListStatus = _salonListStatus.receiveAsFlow()

    private val _likedSalonList = MutableStateFlow(listOf<String>())
    val likedSalonList = _likedSalonList.asStateFlow()

    var salonList by mutableStateOf(listOf<Salon>())
        private set

    init {
        getAllSalonList()
        getAllFavorite()
    }

    private fun getAllSalonList() = viewModelScope.launch {
        firestoreRepository.getAllSalons().collect {
            when (it) {
                is Resource.Error -> {
                    _salonListStatus.send(ResponseStatus(isError = it.message))

                    Log.d("SALON ERROR", it.message.toString())
                }

                is Resource.Loading -> {
                    _salonListStatus.send(ResponseStatus(isLoading = true))
                    Log.d("SALON LOADING", "true")
                }

                is Resource.Success -> {
                    _salonListStatus.send(ResponseStatus(isSuccess = it.data))
                    salonList = it.data ?: emptyList()
                }
            }
        }
    }

    fun addItemToFavorite(salon: Salon, context: Context) = viewModelScope.launch {
        likedItemRepository.addToLiked(salon)
    }.invokeOnCompletion {
        Toast.makeText(context, "Added to liked", Toast.LENGTH_SHORT).show()
    }

    fun removeFromFavorite(salon: Salon, context: Context) = viewModelScope.launch {
        likedItemRepository.deleteFromLiked(salon)
    }.invokeOnCompletion {
        Toast.makeText(context, "Removed from liked", Toast.LENGTH_SHORT).show()
    }

    fun getAllFavorite() = viewModelScope.launch {
        likedItemRepository.getAllLiked().collect { result ->
            _likedSalonList.value = result.map { res ->
                res.itemId
            }
        }

    }
}

data class ResponseStatus(
    val isLoading: Boolean = false,
    val isSuccess: List<Salon>? = null,
    val isError: String? = null
)