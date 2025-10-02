package com.rafarocar.albumlist.feature_albumList.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query

/**
 * Data access object to define the methods to use in the database
 */
@Dao
interface AlbumDao {

    @Query("SELECT * FROM album_table")
    fun getAlbumsPaging(): PagingSource<Int, AlbumEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insertAlbums(albums: List<AlbumEntity>)

    @Query("SELECT * FROM album_table WHERE id = :id")
    suspend fun getAlbumById(id: Int): AlbumEntity

    @Query("UPDATE album_table SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateAlbum(id: Int, isFavorite: Boolean)

    @Query("SELECT * FROM album_table WHERE isFavorite = 1")
    fun getFavoriteAlbums(): PagingSource<Int, AlbumEntity>

    @Query("SELECT * FROM album_table WHERE isFavorite = 1")
    suspend fun getFavoriteAlbumsList(): List<AlbumEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insertAlbum(album: AlbumEntity)

    @Query("DELETE FROM album_table")
    suspend fun clearAll()
}