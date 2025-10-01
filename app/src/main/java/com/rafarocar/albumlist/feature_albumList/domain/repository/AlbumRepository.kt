package com.rafarocar.albumlist.feature_albumList.domain.repository

import androidx.paging.Pager
import com.rafarocar.albumlist.feature_albumList.domain.model.Album


interface AlbumRepository {
    fun getAlbums(): Pager<Int, Album>
}