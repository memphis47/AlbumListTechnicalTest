package com.rafarocar.albumlist.feature_albumList.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Database definition to save the albums and be able to retrieve when offline
 */
@Database(
    entities = [AlbumEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AlbumDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
}