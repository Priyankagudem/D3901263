package uk.ac.tees.mad.d3901263.screens.authentication.domain

data class RegisterState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)