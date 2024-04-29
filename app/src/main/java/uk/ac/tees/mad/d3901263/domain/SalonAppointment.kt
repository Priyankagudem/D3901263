package uk.ac.tees.mad.d3901263.domain

data class SalonAppointment(
    val appointmentId: String ,
    val salonName: String,
    val salonAddress: String,
    val slot: String,
    val customerName: String,
    val customerEmail: String
)