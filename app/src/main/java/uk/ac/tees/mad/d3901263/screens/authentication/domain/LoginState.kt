package uk.ac.tees.mad.d3901263.screens.authentication.domain

data class LoginState(
    val isLoginSuccessful: Boolean = false,
    val loginError: String? = null
)
