package com.rafarocar.albumlist.feature_albumList.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class to define the model of the album in the data layer that come from DB
 */
@Entity(tableName = "album_table")
data class AlbumEntity(
    @PrimaryKey var id: Int = 0,
    var albumId: Int = 0,
    var title: String = "",
    var url: String = "",
    var thumbnailURL: String = "",
    var isFavorite: Boolean = false
)