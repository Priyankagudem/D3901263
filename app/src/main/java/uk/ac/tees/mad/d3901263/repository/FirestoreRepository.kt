package uk.ac.tees.mad.d3901263.repository

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.d3901263.domain.Resource
import uk.ac.tees.mad.d3901263.domain.Salon
import uk.ac.tees.mad.d3901263.domain.SalonAppointment

interface FirestoreRepository {
    fun getAllSalons(): Flow<Resource<List<Salon>>>
    fun getSalonDetails(salonId: String): Flow<Resource<Salon>>
    suspend fun bookAppointment(
        salonId: String,
        userId: String,
        selectedSlot: String
    ): Flow<Resource<String>>
    fun getUserAppointments(userId: String): Flow<Resource<List<SalonAppointment>>>

}