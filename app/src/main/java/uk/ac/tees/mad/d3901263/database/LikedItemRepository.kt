package uk.ac.tees.mad.d3901263.database

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.d3901263.domain.Salon

interface LikedItemRepository {

    suspend fun addToLiked(salon: Salon)

    fun getAllLiked(): Flow<List<LikedItemEntity>>

    suspend fun deleteFromLiked(salon: Salon)
}

class LikedItemRepositoryImpl(
    private val dao: LikedItemDao
) : LikedItemRepository {
    override suspend fun addToLiked(salon: Salon) {
        dao.addToLiked(LikedItemEntity(itemId = salon.id))
    }

    override fun getAllLiked(): Flow<List<LikedItemEntity>> = dao.getAllLiked()

    override suspend fun deleteFromLiked(salon: Salon) =
        dao.deleteFromLiked(LikedItemEntity(itemId = salon.id))

}