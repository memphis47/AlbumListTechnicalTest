package com.rafarocar.albumlist.feature_albumList.domain.model

import java.net.URL

data class Album(
    val albumId: Int,
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailURL: String
)
