package com.rafarocar.albumlist.feature_albumList.data.mapper

import com.rafarocar.albumlist.feature_albumList.data.local.AlbumEntity
import com.rafarocar.albumlist.feature_albumList.data.remote.AlbumDTO
import com.rafarocar.albumlist.feature_albumList.domain.model.Album

/**
 * Extension function to map the DTO to the domain model
 */
fun AlbumDTO.toDomain(): Album =
    Album(
        albumId = albumId,
        id = id,
        title = title,
        url = url,
        thumbnailURL = thumbnailURL
    )

/**
 * Extension function to map the DTO to the entity model
 */
fun AlbumDTO.toEntity(): AlbumEntity =
    AlbumEntity(
        id = id,
        albumId = albumId,
        title = title,
        url = url,
        thumbnailURL = thumbnailURL,
        isFavorite = false
    )

/**
 * Extension function to map the entity to the domain model
 */
fun AlbumEntity.toDomain(): Album =
    Album(
        albumId = albumId,
        id = id,
        title = title,
        url = url,
        thumbnailURL = thumbnailURL,
        isFavorite = isFavorite
    )