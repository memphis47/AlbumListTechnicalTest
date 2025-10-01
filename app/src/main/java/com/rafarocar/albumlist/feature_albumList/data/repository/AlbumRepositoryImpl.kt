package com.rafarocar.albumlist.feature_albumList.data.repository

import com.rafarocar.albumlist.feature_albumList.data.api.AlbumApi
import com.rafarocar.albumlist.feature_albumList.data.model.toDomain
import com.rafarocar.albumlist.feature_albumList.domain.model.Album
import com.rafarocar.albumlist.feature_albumList.domain.repository.AlbumRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlbumRepositoryImpl @Inject constructor(
    private val api: AlbumApi
) : AlbumRepository {

    override suspend fun getAlbums(): List<Album> {
        return api.getAlbums().map { it.toDomain() }
    }
}