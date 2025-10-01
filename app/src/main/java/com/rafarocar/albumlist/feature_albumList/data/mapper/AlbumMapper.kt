package com.rafarocar.albumlist.feature_albumList.data.mapper

import com.rafarocar.albumlist.feature_albumList.data.remote.AlbumDTO
import com.rafarocar.albumlist.feature_albumList.domain.model.Album

fun AlbumDTO.toDomain(): Album =
    Album(
        albumId = albumId,
        id = id,
        title = title,
        url = url,
        thumbnailURL = thumbnailURL
    )