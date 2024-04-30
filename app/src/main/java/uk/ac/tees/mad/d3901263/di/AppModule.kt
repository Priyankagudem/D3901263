package uk.ac.tees.mad.d3901263.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.d3901263.database.LikedItemRepository
import uk.ac.tees.mad.d3901263.database.LikedItemRepositoryImpl
import uk.ac.tees.mad.d3901263.database.LikedItemsDatabase
import uk.ac.tees.mad.d3901263.repository.AuthRepository
import uk.ac.tees.mad.d3901263.repository.AuthRepositoryIMPL
import uk.ac.tees.mad.d3901263.repository.FirestoreRepository
import uk.ac.tees.mad.d3901263.repository.FirestoreRepositoryIMPL
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun providesRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthRepository =
        AuthRepositoryIMPL(firebaseAuth, firestore)

    @Provides
    @Singleton
    fun providesFirestoreRepository(): FirestoreRepository =
        FirestoreRepositoryIMPL()


    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(
            app,
            LikedItemsDatabase::class.java,
            "beauty_appointment_db"
        ).build()

    @Provides
    @Singleton
    fun providesDatabaseRepo(
        db: LikedItemsDatabase
    ): LikedItemRepository =
        LikedItemRepositoryImpl(db.getDao())


}