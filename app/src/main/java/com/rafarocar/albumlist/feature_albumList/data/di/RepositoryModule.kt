package com.rafarocar.albumlist.feature_albumList.data.di

import com.rafarocar.albumlist.feature_albumList.data.repository.AlbumRepositoryImpl
import com.rafarocar.albumlist.feature_albumList.domain.repository.AlbumRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module to bind the repository implementation
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAlbumRepository(
        albumRepositoryImpl: AlbumRepositoryImpl
    ): AlbumRepository

}