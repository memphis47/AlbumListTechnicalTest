package com.rafarocar.albumlist.feature_albumList.data.model

import com.rafarocar.albumlist.feature_albumList.domain.model.Album

fun AlbumDTO.toDomain(): Album =
    Album(
        albumId = albumId,
        id = id,
        title = title,
        url = url,
        thumbnailURL = thumbnailURL
    )