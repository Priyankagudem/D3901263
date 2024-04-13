package uk.ac.tees.mad.d3901263.screens.authentication.domain

data class LoginResult(
    val data: UserData? = null,
    val isSuccessful: Boolean = false,
    val errorMessage: String? = null
)