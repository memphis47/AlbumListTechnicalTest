package com.rafarocar.albumlist.feature_albumList.domain.useCase

import com.rafarocar.albumlist.feature_albumList.domain.repository.AlbumRepository
import javax.inject.Inject

/**
 * Use case to refresh the albums from the API
 * @param repository The repository to use
 */
class RefreshAlbumsUseCase @Inject constructor(
    private val repository: AlbumRepository
) {

    suspend operator fun invoke() {
        return repository.refreshAlbums()
    }
}