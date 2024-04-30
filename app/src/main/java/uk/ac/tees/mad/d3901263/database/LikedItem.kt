package uk.ac.tees.mad.d3901263.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LikedItemEntity(
    @PrimaryKey
    val itemId: String
)