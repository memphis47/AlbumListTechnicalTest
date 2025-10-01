package com.rafarocar.albumlist.feature_albumList.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.rafarocar.albumlist.feature_albumList.data.remote.AlbumApi
import com.rafarocar.albumlist.feature_albumList.data.paging.AlbumPagingSource
import com.rafarocar.albumlist.feature_albumList.domain.model.Album
import com.rafarocar.albumlist.feature_albumList.domain.repository.AlbumRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlbumRepositoryImpl @Inject constructor(
    private val api: AlbumApi
) : AlbumRepository {

    override fun getAlbums(): Pager<Int, Album> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { AlbumPagingSource(api) }
        )
    }
}