package com.rafarocar.albumlist.feature_albumList.data.di

import android.content.Context
import androidx.room.Room
import com.rafarocar.albumlist.feature_albumList.data.local.AlbumDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModules {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AlbumDatabase {
        return Room.databaseBuilder(
            context,
            AlbumDatabase::class.java,
            "album_db"
        ).build()
    }

    @Provides
    fun provideAlbumDao(db: AlbumDatabase) = db.albumDao()
}