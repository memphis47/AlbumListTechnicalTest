package com.rafarocar.albumlist.feature_albumList.domain.repository

import androidx.paging.PagingData
import com.rafarocar.albumlist.feature_albumList.domain.model.Album
import kotlinx.coroutines.flow.Flow

/**
 * Interface to define the methods to use in the repository
 */
interface AlbumRepository {
    fun getAlbums(): Flow<PagingData<Album>>

    suspend fun refreshAlbums()
}