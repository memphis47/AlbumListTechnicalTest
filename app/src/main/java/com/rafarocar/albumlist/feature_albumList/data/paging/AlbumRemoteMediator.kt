package com.rafarocar.albumlist.feature_albumList.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.rafarocar.albumlist.feature_albumList.data.local.AlbumDatabase
import com.rafarocar.albumlist.feature_albumList.data.local.AlbumEntity
import com.rafarocar.albumlist.feature_albumList.data.mapper.toEntity
import com.rafarocar.albumlist.feature_albumList.data.remote.AlbumApi

/**
 * RemoteMediator to handle the data from the API and the database
 * @param albumApi The API to use
 * @param albumDatabase The database to use
 */
@OptIn(ExperimentalPagingApi::class)
class AlbumRemoteMediator(
    private val albumApi: AlbumApi,
    private val albumDatabase: AlbumDatabase
) : RemoteMediator<Int, AlbumEntity>() {

    companion object {
        const val TAG = "AlbumRemoteMediator"
    }

    /**
     * Load the data from the API and the database, also can refresh the data from the API and update the database
     * It use to handle the pagination of the data
     * @param loadType The type of load
     * @param state The state of the load
     */
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AlbumEntity>
    ): MediatorResult {
        return try {
            if (loadType == LoadType.REFRESH) {
                val response = albumApi.getAlbums()

                val albumEntities = response.map { it.toEntity() }

                albumDatabase.withTransaction {
                    albumDatabase.albumDao().clearAll()
                    albumDatabase.albumDao().insertAlbums(albumEntities)
                }
            }

            MediatorResult.Success(endOfPaginationReached = true)
        } catch (e: Exception) {
            Log.e(TAG, "Error loading data: ${e.message}")
            MediatorResult.Error(e)
        }
    }

}