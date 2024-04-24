package uk.ac.tees.mad.d3901263.domain

import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class Salon(
    val id: String,
    val name: String,
    val address: String,
    val rating: Double,
    val imageUrl: String,
    val openTiming: String,
    val closeTiming: String,
    val servicesOffered: List<String>,
    val priceRange: String,
    val slotDuration: Int,
    val description: String
) {
    fun generateAvailableSlotsForDay(bookedSlots: List<LocalTime>): List<LocalTime> {
        val formatter = DateTimeFormatter.ofPattern("H:mm")
        val opening = LocalTime.parse(openTiming, formatter)
        val closing = LocalTime.parse(closeTiming, formatter)

        val slots = mutableListOf<LocalTime>()
        var currentTime = opening
        while (currentTime.plusMinutes(slotDuration.toLong())
                .isBefore(closing) || currentTime.plusMinutes(slotDuration.toLong()).equals(closing)
        ) {
            if (!bookedSlots.contains(currentTime)) {
                slots.add(currentTime)
            }
            currentTime = currentTime.plusMinutes(slotDuration.toLong())
        }
        return slots
    }
}

val salonListtt = listOf<Salon>(
    Salon(
        "1",
        "Elegant Styles",
        "123 High Street, London",
        4.8,
        "https://cdn1.treatwell.net/images/view/v2.i1719934.w720.h480.x8E77FCF0/",
        "9am ",
        " 8pm",
        listOf("Haircut", "Manicure"),
        "€20 - €40",
        30,
        "Elegant Styles offers a luxurious experience with top-notch hair and nail services in the heart of London. Our expert stylists ensure you leave looking and feeling fabulous."

    ),
    Salon(
        "2",
        "The Beauty Lounge",
        "45 Queen Street, Manchester",
        4.5,
        "https://d1ooscleda9ip9.cloudfront.net/Upload/669/Documents/Blend%20makeup.png",
        "10am ", " 9pm",
        listOf("Facial", "Pedicure"),
        "€30 - €40",
        30,
        "Escape the hustle and bustle of Edinburgh with a visit to The Pamper Room. Our serene environment and skilled therapists are here to provide the ultimate relaxation experience."

    ),
    Salon(
        "3",
        "Glamour Studio",
        "67 King's Road, Birmingham",
        4.7,
        "https://media.vogue.co.uk/photos/643ead07a58bb417fa875418/master/w_1600%2Cc_limit/ADAM%2520REED%2520SALON%2520LONG%2520ACRE-123HDR.jpg",
        "8am ",
        " 6pm",
        listOf("Makeup", "Hair Styling"),
        "€20 - €60",
        30,
        "Urban Retreat in Manchester is your go-to destination for all things beauty. From stunning hair transformations to flawless makeup applications, we've got you covered for every occasion."

    ),
    Salon(
        "4",
        "Pure Bliss",
        "89 Park Avenue, Liverpool",
        4.3,
        "https://www.hairgallery.co.uk/wp-content/uploads/2021/11/hair-gallery-and-gallery-day-spa-sm.jpg",
        "10am ", " 8pm",
        listOf("Massage", "Spa"),
        "€30 - €40",
        30,
        "Elegant Styles offers a luxurious experience with top-notch hair and nail services in the heart of London. Our expert stylists ensure you leave looking and feeling fabulous."

    ),
    Salon(
        "5",
        "Urban Retreat",
        "234 Baker Street, Newcastle",
        4.6,
        "https://media.istockphoto.com/id/1139132195/photo/modern-hair-salon.jpg?s=612x612&w=0&k=20&c=5f6Ep31GWb3ZmQwtPpvBBVAXpLgJ5b9DKT0CGYvKevo=",
        "9am ", " 7pm",
        listOf("Haircut", "Beard Trim"),
        "€20 - €60",
        30,
        "Escape the hustle and bustle of Edinburgh with a visit to The Pamper Room. Our serene environment and skilled therapists are here to provide the ultimate relaxation experience."

    ),
    Salon(
        "6",
        "The Grooming Room",
        "78 Oxford Street, London",
        4.9,
        "https://comfortel.co.uk/wp-content/uploads/3-Coastal-Salon-Furniture-Interior-Design-Salon.jpg",
        "10am ", " 9pm",
        listOf("Haircut", "Facial"),
        "€20 - €60",
        30,
        "Urban Retreat in Manchester is your go-to destination for all things beauty. From stunning hair transformations to flawless makeup applications, we've got you covered for every occasion."
    ),
    Salon(
        "7",
        "Serenity Now",
        "56 Elm Road, Sheffield",
        4.2,
        "https://retaildesignblog.net/wp-content/uploads/2014/05/Aveda-Lifestyle-salon-Spa-by-Reis-Design-London-UK.jpg",
        "8am ", " 8pm",
        listOf("Spa", "Pedicure"),
        "€20 - €40",
        30,
        "Elegant Styles offers a luxurious experience with top-notch hair and nail services in the heart of London. Our expert stylists ensure you leave looking and feeling fabulous."

    ),
    Salon(
        "8",
        "Revive Beauty",
        "12 Duke Street, Edinburgh",
        4.4,
        "https://upload.wikimedia.org/wikipedia/commons/6/69/Uk_salon_and_beauty_spa_shop.jpg",
        "9am ", " 6pm",
        listOf("Manicure", "Facial"),
        "€20 - €40",
        30,
        "Escape the hustle and bustle of Edinburgh with a visit to The Pamper Room. Our serene environment and skilled therapists are here to provide the ultimate relaxation experience."

    ),
    Salon(
        "9",
        "The Makeover Place",
        "90 Piccadilly, Manchester",
        4.5,
        "https://i.pinimg.com/736x/dc/4a/0d/dc4a0dce04515a4d6d1090c70a902310.jpg",
        "10am ", " 8pm",
        listOf("Makeup", "Hair Styling"),
        "€20 - €40",
        30,
        "Urban Retreat in Manchester is your go-to destination for all things beauty. From stunning hair transformations to flawless makeup applications, we've got you covered for every occasion."
    ),
    Salon(
        "10",
        "Hair and Now",
        "33 Castle Street, Liverpool",
        4.7,
        "https://comfortel.co.uk/wp-content/uploads/3-Coastal-Salon-Furniture-Interior-Design-Salon.jpg",
        "9am ", " 7pm",
        listOf("Haircut", "Coloring"),
        "€20 - €40",
        30,
        "Elegant Styles offers a luxurious experience with top-notch hair and nail services in the heart of London. Our expert stylists ensure you leave looking and feeling fabulous."

    ),
    Salon(
        "11",
        "Beauty & Beyond",
        "77 Sunset Boulevard, Bristol",
        4.6,
        "https://cdn1.treatwell.net/images/view/v2.i1719934.w720.h480.x8E77FCF0/",
        "8am ", " 8pm",
        listOf("Facial", "Massage"),
        "€20 - €40",
        30,
        "Escape the hustle and bustle of Edinburgh with a visit to The Pamper Room. Our serene environment and skilled therapists are here to provide the ultimate relaxation experience."

    ),
    Salon(
        "12",
        "Next Level Looks",
        "66 Vine Street, Glasgow",
        4.8,
        "https://i.pinimg.com/736x/dc/4a/0d/dc4a0dce04515a4d6d1090c70a902310.jpg",
        "10am ", " 9pm",
        listOf("Hair Styling", "Manicure"),
        "€20 - €40",
        30,
        "Urban Retreat in Manchester is your go-to destination for all things beauty. From stunning hair transformations to flawless makeup applications, we've got you covered for every occasion."

    ),
    Salon(
        "13",
        "The Pamper Zone",
        "101 High Street, Cardiff",
        4.3,
        "https://www.hairgallery.co.uk/wp-content/uploads/2021/11/hair-gallery-and-gallery-day-spa-sm.jpg",
        "9am ", " 6pm",
        listOf("Spa", "Pedicure"),
        "€20 - €40",
        30,
        "Elegant Styles offers a luxurious experience with top-notch hair and nail services in the heart of London. Our expert stylists ensure you leave looking and feeling fabulous."

    ),
    Salon(
        "14",
        "Chic Cuts",
        "88 Regent Street, London",
        4.9,
        "https://comfortel.co.uk/wp-content/uploads/3-Coastal-Salon-Furniture-Interior-Design-Salon.jpg",
        "10am ", " 8pm",
        listOf("Haircut", "Beard Trim"),
        "€20 - €40",
        30,
        "Escape the hustle and bustle of Edinburgh with a visit to The Pamper Room. Our serene environment and skilled therapists are here to provide the ultimate relaxation experience."

    ),
    Salon(
        "15",
        "Lavish Locks",
        "59 Broad Street, Nottingham",
        4.4,
        "https://www.hairgallery.co.uk/wp-content/uploads/2021/11/hair-gallery-and-gallery-day-spa-sm.jpg",
        "8am ", " 7pm",
        listOf("Haircut", "Hair Treatment"),
        "€20 - €40",
        30,
        "Urban Retreat in Manchester is your go-to destination for all things beauty. From stunning hair transformations to flawless makeup applications, we've got you covered for every occasion."

    )
)