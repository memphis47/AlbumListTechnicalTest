package com.rafarocar.albumlist.feature_albumList.data.di

import com.rafarocar.albumlist.feature_albumList.data.remote.AlbumApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAlbumRepository(retrofit: Retrofit): AlbumApi =
        retrofit.create(AlbumApi::class.java)

}