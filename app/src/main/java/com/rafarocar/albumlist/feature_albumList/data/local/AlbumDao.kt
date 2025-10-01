package com.rafarocar.albumlist.feature_albumList.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query

@Dao
interface AlbumDao {

    @Query("SELECT * FROM album_table")
    fun getAlbumsPaging(): PagingSource<Int, AlbumEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insertAlbums(albums: List<AlbumEntity>)
}