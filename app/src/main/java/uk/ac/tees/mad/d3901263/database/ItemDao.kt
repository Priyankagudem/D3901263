package uk.ac.tees.mad.d3901263.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.d3901263.database.LikedItemEntity

@Dao
interface LikedItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToLiked(favoriteEntity: LikedItemEntity)

    @Query("select * from LikedItemEntity")
    fun getAllLiked(): Flow<List<LikedItemEntity>>

    @Delete
    suspend fun deleteFromLiked(favoriteEntity: LikedItemEntity)

}