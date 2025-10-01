package com.rafarocar.albumlist.feature_albumList.domain.repository

import com.rafarocar.albumlist.feature_albumList.domain.model.Album


interface AlbumRepository {
    suspend fun getAlbums(): List<Album>
}