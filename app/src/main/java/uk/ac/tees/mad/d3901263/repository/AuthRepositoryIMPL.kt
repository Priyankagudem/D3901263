package uk.ac.tees.mad.d3901263.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.d3901263.domain.FirestoreUser
import uk.ac.tees.mad.d3901263.domain.Resource
import uk.ac.tees.mad.d3901263.domain.UserDetailUiState
import java.util.UUID
import javax.inject.Inject


class AuthRepositoryIMPL @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : AuthRepository {

    override fun loginUser(email: String, password: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun registerUser(
        email: String,
        password: String,
        username: String
    ): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            // Add user to Firestore with username
            val userId = authResult.user?.uid
            saveUser(userId = userId, email = email, username = username)
            emit(Resource.Success(authResult))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override suspend fun saveUser(email: String?, username: String?, userId: String?) {
        if (userId != null) {
            val userMap = hashMapOf(
                "email" to email,
                "username" to username
                // Add other user data if needed
            )
            firebaseFirestore.collection("users").document(userId).set(userMap).await()
        }
    }


    override fun getCurrentUser(): Flow<Resource<FirestoreUser>> = callbackFlow {
        trySend(Resource.Loading())
        val currentUserUid = firebaseAuth.currentUser?.uid
        if (currentUserUid != null) {
            firebaseFirestore.collection("users").document(currentUserUid).get()
                .addOnSuccessListener { mySnapshot ->
                    if (mySnapshot.exists()) {
                        val data = mySnapshot.data

                        if (data != null) {
                            val userResponse = FirestoreUser(
                                key = currentUserUid,
                                item = FirestoreUser.UserDetail(
                                    name = data["name"] as String? ?: "",
                                    email = data["email"] as String? ?: "",
                                    phone = data["phone"] as String? ?: "",
                                    dob = data["dob"] as String? ?: "",
                                    gender = data["gender"] as String? ?: "",
                                    image = data["image"] as String? ?: ""
                                )
                            )

                            trySend(Resource.Success(userResponse))
                        } else {
                            println("No data found in Database")
                        }
                    } else {
                        println("No data found in Database")
                    }
                }.addOnFailureListener { e ->
                    Log.d("ERRor", e.toString())
                    trySend(Resource.Error(message = e.toString()))
                }
        } else {
            trySend(Resource.Error(message = "User not signed up"))
        }
        awaitClose {
            close()
        }
    }

    override fun updateCurrentUser(item: UserDetailUiState): Flow<Resource<String>> =
        callbackFlow {
            trySend(Resource.Loading())
            Log.d("UPDATE", "UPDATE USER INVOKED")
            val currentUserUid = firebaseAuth.currentUser?.uid

            val storage = Firebase.storage.reference
            val imageRef = storage.child("user/${UUID.randomUUID()}")
            val uploadTask = item.image?.let {
                imageRef.putBytes(it)
            }

            uploadTask?.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val map = HashMap<String, Any>()
                    map["name"] = item.name
                    map["email"] = item.email
                    map["phone"] = item.phone
                    map["dob"] = item.dob
                    map["gender"] = item.gender
                    map["image"] = uri.toString()
                    if (currentUserUid != null) {
                        firebaseFirestore.collection("users")
                            .document(currentUserUid)
                            .set(map)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Log.d("UPDATE", "$map")

                                    trySend(Resource.Success("Updated Successfully.."))
                                }
                            }
                            .addOnFailureListener { e ->
                                trySend(Resource.Error(message = e.message))
                            }
                    } else {
                        trySend(Resource.Error(message = "User not logged in"))
                    }
                }
            }?.addOnFailureListener { e ->
                trySend(Resource.Error(message = e.message))
            }

            awaitClose { close() }
        }
}