package uk.ac.tees.mad.d3901263.screens.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3901263.domain.FirestoreUser
import uk.ac.tees.mad.d3901263.domain.Resource
import uk.ac.tees.mad.d3901263.domain.UserDetailUiState
import uk.ac.tees.mad.d3901263.repository.AuthRepository
import uk.ac.tees.mad.d3901263.repository.FirestoreRepository
import java.io.ByteArrayOutputStream
import java.net.URL
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val authRepository: AuthRepository
) : ViewModel() {


    private val _currentUserState = Channel<FirestoreUserState>()
    val currentUserState = _currentUserState.receiveAsFlow()

    private val _userUpdateState = Channel<UpdateResponseState>()
    val userUpdateState = _userUpdateState.receiveAsFlow()

    private val _profileUiState = MutableStateFlow(UserDetailUiState())
    val profileUiState = _profileUiState.asStateFlow()

    fun updateProfileUiState(uiState: UserDetailUiState) {
        _profileUiState.value = uiState
    }

    init {
        getUserInformation()
    }

    fun getUserInformation() = viewModelScope.launch {
        authRepository.getCurrentUser().collect {
            when (it) {
                is Resource.Error -> {
                    _currentUserState.send(FirestoreUserState(error = it.message))
                }

                is Resource.Loading -> {
                    _currentUserState.send(FirestoreUserState(isLoading = true))
                }

                is Resource.Success -> {
                    _currentUserState.send(FirestoreUserState(data = it.data))
                    launch(Dispatchers.IO) {
                        _profileUiState.update { state ->
                            val item = it.data?.item
                            state.copy(
                                name = item?.name ?: "",
                                email = item?.email ?: "",
                                phone = item?.phone ?: "",
                                dob = item?.dob ?: "",
                                gender = item?.gender ?: "",
                                image = getImageFromUrl(item?.image ?: "")
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateUser() = viewModelScope.launch {
        authRepository.updateCurrentUser(_profileUiState.value).collect {
            when (it) {
                is Resource.Error -> {
                    _userUpdateState.send(UpdateResponseState(error = it.message))
                }

                is Resource.Loading -> {
                    _userUpdateState.send(UpdateResponseState(isLoading = true))
                }

                is Resource.Success -> {
                    _userUpdateState.send(UpdateResponseState(data = it.data))
                }
            }
        }
    }


    fun handleImageSelection(uri: Uri, context: Context) {
        val bitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images
                .Media
                .getBitmap(context.contentResolver, uri)

        } else {
            val source = ImageDecoder
                .createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }
        val imageByteArray = convertBitmapToByteArray(bitmap)
        _profileUiState.update {
            it.copy(image = imageByteArray)
        }
    }

    fun handleImageCapture(bitmap: Bitmap) {
        val imageByteArray = convertBitmapToByteArray(bitmap)
        _profileUiState.update {
            it.copy(image = imageByteArray)
        }
    }

}

fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return outputStream.toByteArray()
}

private fun getImageFromUrl(url: String): ByteArray? {
    try {
        val imageUrl = URL(url)
        val connection = imageUrl.openConnection()
        val inputStream = connection.getInputStream()
        val outputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var read = 0
        while (inputStream.read(buffer, 0, buffer.size).also { read = it } != -1) {
            outputStream.write(buffer, 0, read)
        }
        outputStream.flush()
        return outputStream.toByteArray()
    } catch (e: Exception) {
        Log.d("ImageManager", "Error: $e")
    }
    return null
}

data class FirestoreUserState(
    val data: FirestoreUser? = null,
    val error: String? = null,
    val isLoading: Boolean = false
)

data class UpdateResponseState(
    val data: String? = null,
    val error: String? = null,
    val isLoading: Boolean = false
)