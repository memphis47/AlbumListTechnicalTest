package com.rafarocar.albumlist.feature_albumList.domain.useCase

import com.rafarocar.albumlist.feature_albumList.domain.repository.AlbumRepository
import javax.inject.Inject

class GetAlbumsUseCase @Inject constructor(
    private val repository: AlbumRepository
)
{
    suspend operator fun invoke() = repository.getAlbums()
}