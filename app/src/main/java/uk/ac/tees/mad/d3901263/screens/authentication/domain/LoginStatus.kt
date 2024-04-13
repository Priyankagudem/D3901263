package uk.ac.tees.mad.d3901263.screens.authentication.domain

data class LoginStatus(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)