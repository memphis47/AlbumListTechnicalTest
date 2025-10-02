package com.rafarocar.albumlist.feature_albumList.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.rafarocar.albumlist.feature_albumList.data.local.AlbumDatabase
import com.rafarocar.albumlist.feature_albumList.data.mapper.toDomain
import com.rafarocar.albumlist.feature_albumList.data.mapper.toEntity
import com.rafarocar.albumlist.feature_albumList.data.paging.AlbumRemoteMediator
import com.rafarocar.albumlist.feature_albumList.data.remote.AlbumApi
import com.rafarocar.albumlist.feature_albumList.domain.model.Album
import com.rafarocar.albumlist.feature_albumList.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository implementation to handle the data from the API and the database
 * It can get albums from the Api or from the database, also can refresh the albums from the API and update the DB
 */
@Singleton
@OptIn(ExperimentalPagingApi::class)
class AlbumRepositoryImpl @Inject constructor(
    private val api: AlbumApi,
    private val albumDatabase: AlbumDatabase
) : AlbumRepository {

    /**
     * Get the albums from the database
     */
    override fun getAlbums(): Flow<PagingData<Album>> {
        val pagingSourceFactory = { albumDatabase.albumDao().getAlbumsPaging() }
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = AlbumRemoteMediator(api, albumDatabase),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    /**
     * Refresh the albums from the API and update the database
     */
    override suspend fun refreshAlbums() {
        val albums = api.getAlbums()
        albumDatabase.albumDao().insertAlbums(albums.map { it.toEntity() })
    }
}