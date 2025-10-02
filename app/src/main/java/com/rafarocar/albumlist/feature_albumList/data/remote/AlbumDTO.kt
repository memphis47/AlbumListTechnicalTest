package com.rafarocar.albumlist.feature_albumList.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data class to define the model of the album in the data layer that come from API
 */
@JsonClass(generateAdapter = true)
data class AlbumDTO(
    @Json(name = "albumId") val albumId: Int,
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "url") val url: String,
    @Json(name = "thumbnailUrl") val thumbnailURL: String
)