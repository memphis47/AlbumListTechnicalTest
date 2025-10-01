package com.rafarocar.albumlist.feature_albumList.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album_table")
class AlbumEntity {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var albumId: Int = 0
    var title: String = ""
    var url: String = ""
    var thumbnailURL: String = ""
    var isFavorite: Boolean = false
}