package com.rafarocar.albumlist.feature_albumList.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rafarocar.albumlist.feature_albumList.data.remote.AlbumApi
import com.rafarocar.albumlist.feature_albumList.data.mapper.toDomain
import com.rafarocar.albumlist.feature_albumList.domain.model.Album
import java.lang.Exception

class AlbumPagingSource(
    private val api: AlbumApi
): PagingSource<Int, Album>() {

    companion object {
        const val TAG = "AlbumPagingSource"
    }

    override fun getRefreshKey(state: PagingState<Int, Album>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Album> {
        return try {
            val albumsDto = api.getAlbums()
            val albums = albumsDto.map { it.toDomain() }
            LoadResult.Page(
                data = albums,
                prevKey = null,
                nextKey = null
            )
        } catch (e : Exception) {
            Log.e(TAG, e.message.toString())
            LoadResult.Error(e)
        }
    }

}