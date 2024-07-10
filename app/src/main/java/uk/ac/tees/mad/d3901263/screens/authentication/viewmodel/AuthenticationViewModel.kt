package uk.ac.tees.mad.d3901263.screens.authentication.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3901263.domain.Resource
import uk.ac.tees.mad.d3901263.repository.AuthRepository
import uk.ac.tees.mad.d3901263.screens.authentication.domain.LoginResult
import uk.ac.tees.mad.d3901263.screens.authentication.domain.LoginState
import uk.ac.tees.mad.d3901263.screens.authentication.domain.LoginStatus
import uk.ac.tees.mad.d3901263.screens.authentication.domain.LoginUiState
import uk.ac.tees.mad.d3901263.screens.authentication.domain.RegisterState
import uk.ac.tees.mad.d3901263.screens.authentication.domain.RegisterUiState
import uk.ac.tees.mad.d3901263.screens.authentication.domain.UserData
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val repository: AuthRepository,
    firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _loginStatus = Channel<LoginStatus>()
    val loginState = _loginStatus.receiveAsFlow()

    private val _signupUiState = MutableStateFlow(RegisterUiState())
    val registerUiState = _signupUiState.asStateFlow()

    private val _registerState = Channel<RegisterState>()
    val registerState = _registerState.receiveAsFlow()

    fun updateLoginState(value: LoginUiState) {
        _loginUiState.value = value
    }

    fun onLoginWithGoogleResult(result: LoginResult) {
        _state.update {
            it.copy(
                isLoginSuccessful = result.data != null, loginError = result.errorMessage
            )
        }
    }

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        repository.loginUser(email, password).collect { result ->
            when (result) {
                is Resource.Error -> {
                    _loginStatus.send(LoginStatus(isError = result.message))
                }

                is Resource.Loading -> {
                    _loginStatus.send(LoginStatus(isLoading = true))
                }

                is Resource.Success -> {
                    _loginStatus.send(LoginStatus(isSuccess = "Sign In Success"))

                }
            }
        }
    }


    fun saveUserInFirestore(user: UserData) = viewModelScope.launch {
        repository.saveUser(email = user.email, username = user.username, userId = user.userId)
    }

    var username = mutableStateOf("Guest")
    private var myDatabase = Firebase.firestore
    private val uid = firebaseAuth.currentUser?.uid

    init {
        getUserDetails()
    }

    private fun getUserDetails() {
        viewModelScope.launch {
            if (uid != null) {
                myDatabase.collection("users").document(uid).get()
                    .addOnSuccessListener { mySnapshot ->
                        Log.d("USER", "$mySnapshot")

                        if (mySnapshot.exists()) {
                            val list = mySnapshot.data
                            if (list != null) {
                                for (items in list) {
                                    if (items.key == "username") {
                                        username.value = items.value.toString()
                                    }
                                }
                            }
                        } else {
                            println("No data found in Database")
                        }
                    }.addOnFailureListener {
                        println("$it")
                    }
            }

        }
    }

    fun updateRegisterState(value: RegisterUiState) {
        _signupUiState.value = value
    }

    fun registerUser(email: String, password: String, username: String) = viewModelScope.launch {
        repository.registerUser(email, password, username).collect { result ->
            when (result) {
                is Resource.Error -> {
                    _registerState.send(RegisterState(isError = result.message))
                }

                is Resource.Loading -> {
                    _registerState.send(RegisterState(isLoading = true))
                }

                is Resource.Success -> {
                    _registerState.send(RegisterState(isSuccess = "Register Success"))
                }
            }
        }
    }
}