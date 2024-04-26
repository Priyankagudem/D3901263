package uk.ac.tees.mad.d3901263.domain


data class FirestoreUser(
    val item: UserDetail?,
    val key: String?
) {
    data class UserDetail(
        val name: String = "",
        val email: String = "",
        val phone: String = "",
        val dob: String = "",
        val gender: String = "",
        val image: String = ""
    )
}

data class UserDetailUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val dob: String = "",
    val gender: String = "",
    val image: ByteArray? = null
)