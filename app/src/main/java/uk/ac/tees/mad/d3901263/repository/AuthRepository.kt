package uk.ac.tees.mad.d3901263.repository

import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.d3901263.domain.FirestoreUser
import uk.ac.tees.mad.d3901263.domain.Resource
import uk.ac.tees.mad.d3901263.domain.UserDetailUiState

interface AuthRepository {
    fun loginUser(email: String, password: String): Flow<Resource<AuthResult>>
    fun registerUser(email: String, password: String, username: String): Flow<Resource<AuthResult>>
    suspend fun saveUser(email: String?, username: String?, userId: String?)
    fun getCurrentUser(): Flow<Resource<FirestoreUser>>

    fun updateCurrentUser(
        item: UserDetailUiState
    ): Flow<Resource<String>>
}