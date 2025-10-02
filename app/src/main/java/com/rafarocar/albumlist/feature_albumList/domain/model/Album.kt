package com.rafarocar.albumlist.feature_albumList.domain.model

/**
 * Data class to define the model of the album in the domain layer
 */
data class Album(
    val albumId: Int,
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailURL: String,
    val isFavorite: Boolean = false
)
