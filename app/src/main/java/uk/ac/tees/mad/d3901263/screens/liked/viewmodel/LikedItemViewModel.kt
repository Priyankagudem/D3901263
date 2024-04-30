package uk.ac.tees.mad.d3901263.screens.liked.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3901263.database.LikedItemRepository
import uk.ac.tees.mad.d3901263.domain.Resource
import uk.ac.tees.mad.d3901263.domain.Salon
import uk.ac.tees.mad.d3901263.repository.FirestoreRepository
import javax.inject.Inject

@HiltViewModel
class LikedItemViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val likedItemRepository: LikedItemRepository
) : ViewModel() {

    init {
        loadAllLikedItems()
    }

    private val _likedItemsListState = Channel<FirestoreResponseItemState>()
    val likedItemsListState = _likedItemsListState.receiveAsFlow()

    var allFavorites by mutableStateOf(listOf<String>())
        private set

    fun loadAllLikedItems() {
        viewModelScope.launch {
            likedItemRepository.getAllLiked().onEach {
                allFavorites = it.map { favorite ->
                    favorite.itemId
                }
                getLikedItemList()
            }.launchIn(viewModelScope)
        }
    }

    private fun getLikedItemList() = viewModelScope.launch {
        firestoreRepository.getSalonListByKey(allFavorites).collect {
            when (it) {
                is Resource.Error -> {
                    _likedItemsListState.send(FirestoreResponseItemState(isError = it.message))
                }

                is Resource.Loading -> {
                    _likedItemsListState.send(FirestoreResponseItemState(isLoading = true))

                }

                is Resource.Success -> {
                    _likedItemsListState.send(FirestoreResponseItemState(isSuccess = it.data))
                }
            }
        }
    }

    fun deleteFromFavorite(item: Salon, context: Context) = viewModelScope.launch {
        likedItemRepository.deleteFromLiked(item)
    }.invokeOnCompletion {
        Toast.makeText(context, "Removed from liked", Toast.LENGTH_SHORT).show()
        loadAllLikedItems()
    }
}

data class FirestoreResponseItemState(
    val isLoading: Boolean = false,
    val isSuccess: List<Salon>? = null,
    val isError: String? = null
)