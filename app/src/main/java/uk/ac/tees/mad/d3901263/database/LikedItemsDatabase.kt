package uk.ac.tees.mad.d3901263.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LikedItemEntity::class], version = 1, exportSchema = false)
abstract class LikedItemsDatabase : RoomDatabase() {
    abstract fun getDao(): LikedItemDao
}