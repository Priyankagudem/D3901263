package uk.ac.tees.mad.d3901263.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.d3901263.domain.Resource
import uk.ac.tees.mad.d3901263.domain.Salon


class FirestoreRepositoryIMPL : FirestoreRepository {
    override fun getAllSalons(): Flow<Resource<List<Salon>>> = callbackFlow {
        trySend(Resource.Loading())
        try {
            val documents = FirebaseFirestore.getInstance().collection("salons").get().await()
            val salons = documents.mapNotNull { document ->
                val data = document.data
                Salon(
                    id = document.id,
                    name = data["name"] as String? ?: "Unknown Salon",
                    address = data["address"] as String? ?: "Unknown Address",
                    rating = (data["rating"] as Number?)?.toDouble() ?: 0.0,
                    imageUrl = data["imageUrl"] as String?
                        ?: "https://example.com/default_image.jpg",
                    openTiming = data["openingTime"] as String? ?: "9:00",
                    closeTiming = data["closingTime"] as String? ?: "18:00",
                    servicesOffered = (data["services"] as List<String>?)
                        ?: listOf("General Service"),
                    priceRange = data["priceRange"] as String? ?: "$0 - $0",
                    slotDuration = (data["slotDuration"] as Number?)?.toInt() ?: 30,
                    description = data["description"] as String? ?: "Unknown Salon Description"
                )
            }
            trySend(Resource.Success(salons))
        } catch (ex: Exception) {
            ex.printStackTrace()
            trySend(Resource.Error("Error fetching Salons"))
        }
        awaitClose { close() }
    }


    override fun getSalonDetails(salonId: String): Flow<Resource<Salon>> = callbackFlow {
        trySend(Resource.Loading())

        FirebaseFirestore.getInstance().collection("salons").document(salonId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val data = document.data
                    if (data != null) {
                        val salon = Salon(
                            id = document.id,
                            name = data["name"] as String? ?: "Unknown Salon",
                            address = data["address"] as String? ?: "Unknown Address",
                            rating = (data["rating"] as Number?)?.toDouble() ?: 0.0,
                            imageUrl = data["imageUrl"] as String?
                                ?: "https://example.com/default_image.jpg",
                            openTiming = data["openingTime"] as String? ?: "9:00",
                            closeTiming = data["closingTime"] as String? ?: "18:00",
                            servicesOffered = (data["services"] as List<String>?)
                                ?: listOf("General Service"),
                            priceRange = data["priceRange"] as String? ?: "$0 - $0",
                            slotDuration = (data["slotDuration"] as Number?)?.toInt() ?: 30,
                            description = data["description"] as String?
                                ?: "Unknown Salon Description"
                        )
                        trySend(Resource.Success(salon))
                    } else {
                        trySend(Resource.Error("Data is null"))
                    }
                } else {
                    trySend(Resource.Error("Salon not found"))
                }
            }
            .addOnFailureListener { e ->
                trySend(Resource.Error("Failed to get salon details: ${e.message}"))
            }

        awaitClose { close() }
    }


    override suspend fun bookAppointment(
        salonId: String,
        userId: String,
        selectedSlot: String
    ): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading())
        val db = FirebaseFirestore.getInstance()
        try {
            // Fetch user details
            db.collection("users").document(userId).get().addOnSuccessListener { userDocument ->
                val userName = userDocument.getString("username") ?: "Unknown User"
                val userEmail = userDocument.getString("email") ?: "No Email"
                val bookingInfo = hashMapOf(
                    "userName" to userName,
                    "userEmail" to userEmail,
                    "userId" to userDocument.id,
                    "selectedSlot" to selectedSlot,
                    "salonId" to salonId
                )
                db.collection("appointments").add(bookingInfo).addOnSuccessListener {
                    trySend(Resource.Success("Booking successful"))
                    println("Booking successful for $userName at $selectedSlot")
                }.addOnFailureListener {
                    trySend(Resource.Success("Booking failed"))
                }
            }.addOnFailureListener {
                trySend(Resource.Success("Booking failed"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            trySend(Resource.Success("Booking failed"))
            println("Failed to book appointment: ${e.message}")
        }
        awaitClose { close() }
    }
}